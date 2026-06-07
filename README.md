# when_bus

1601번 버스의 실시간 도착 정보를 주기적으로 수집해, 날짜별 실제 출발 히스토리로 보여주는 버스 시간표 기록 서비스입니다.

> 현재 서비스 운영은 종료되었습니다. 이 저장소는 Spring Boot 백엔드와 배포 당시 사용한 Flutter Web 정적 빌드 결과물을 보존한 프로젝트입니다.

## 프로젝트 정보

- 개발 기간: 2023.02.14 ~ 2023.03.14
- 백엔드 저장소: 현재 저장소
- Flutter 클라이언트 저장소: https://github.com/oyc0401/when_bus_flutter
- 운영 URL: `http://3.36.184.88:8080` (운영 종료)

## 문제 정의

고정 시간표만으로는 특정 정류장에서 1601번 버스가 실제로 언제 출발했는지, 배차 간격이 어느 정도였는지 확인하기 어렵습니다.

실시간 버스 도착 API는 현재 도착 예정 정보를 제공하지만, 사용자가 나중에 날짜별 운행 기록을 다시 확인할 수 있는 형태로 누적되지는 않습니다. `when_bus`는 실시간 도착 정보를 일정 주기로 조회하고, 출발로 판단되는 이벤트를 저장해 일자별 시간표 히스토리로 조회할 수 있도록 만든 프로젝트입니다.

## 주요 기능

- 1601번 버스 도착 정보 주기 수집
- 수집 시작/중지 및 현재 수집 상태 확인 API 제공
- 도착 메시지를 기반으로 실제 운행 이벤트 필터링
- 출발 시각을 10분 단위로 보정해 시간표 형태로 저장
- 이전 출발 기록과 비교해 배차 간격 계산
- 날짜별 출발 히스토리 조회
- 조회 가능한 날짜 목록 제공
- Spring Boot 서버에서 Flutter Web 정적 파일 제공

## 기술 스택

### Backend

- Java 17
- Spring Boot 3.0.1
- Spring MVC
- Spring WebFlux `WebClient`
- Spring Data JPA
- Gradle

### Database

- MariaDB
- H2

### Frontend

- Flutter Web
- Spring Boot static resource serving

### Infra

- AWS EC2
- AWS RDS MariaDB

## 동작 흐름

```text
서울 버스 도착 정보 API
        ↓
BusService 수집 스레드
        ↓
도착 메시지 확인
        ↓
출발 이벤트 판단 / 시간 보정 / 배차 간격 계산
        ↓
MariaDB 저장
        ↓
Spring REST API
        ↓
Flutter Web 클라이언트
```

## 핵심 구현

### 1. 실시간 도착 정보 수집

`BusService`는 `ExecutorService` 기반 단일 스레드에서 수집 작업을 실행합니다. 수집이 시작되면 90초 간격으로 서울 버스 도착 정보 API를 호출하고, 0시부터 3시까지는 수집을 건너뜁니다.

```java
private ExecutorService executorService = Executors.newSingleThreadExecutor();
private boolean doing = false;
```

수집 제어 API는 `/api/start`, `/api/stop`, `/api/working`으로 분리되어 있어 서버 실행 중에도 수집 작업을 시작하거나 중지할 수 있습니다.

### 2. 출발 이벤트 판단

버스 API 응답의 `arrmsg1`, `vehId1`, `plainNo1` 값을 읽어 현재 버스 상태, 차량 ID, 차량 번호를 추출합니다. 메시지가 `출발대기` 또는 `운행종료`가 아닌 경우 실제 운행 중인 버스로 판단해 저장 대상으로 처리합니다.

```java
if (!message.equals("출발대기") && !message.equals("운행종료")) {
    Bus newBus = new Bus();
    newBus.setBusId(vehId);
    newBus.setDepartAt(roundMinute10(now));
    newBus.setCreateAt(now);
    newBus.setMessage(message);
    newBus.setBusNum(busNum);
}
```

### 3. 중복 저장 방지

가장 최근에 저장된 버스와 새로 감지된 버스의 차량 ID, 날짜를 비교합니다. 같은 날짜에 같은 차량이 다시 감지되면 중복으로 판단해 저장하지 않습니다.

```java
if (m.getBusId() == bus.getBusId()
        && m.getCreateAt().getDayOfYear() == bus.getCreateAt().getDayOfYear()) {
    throw new IllegalStateException("최근에 추가한 버스입니다.");
}
```

### 4. 시간표 히스토리용 데이터 가공

수집 시각을 그대로 저장하지 않고, 시간표로 보기 쉽도록 10분 단위로 보정합니다. 또한 직전 출발 기록과의 차이를 계산해 `busInterval`에 저장합니다.

```java
Duration duration = Duration.between(b.getDepartAt(), newBus.getDepartAt());
long minutes = duration.toMinutes();
newBus.setBusInterval((int) minutes);
```

### 5. 날짜별 히스토리 조회

`/history?date=yyyyMMdd` 요청을 받으면 해당 날짜의 00:00:00부터 23:59:59까지 `departAt` 범위에 포함되는 버스 기록을 조회합니다.

```java
LocalDateTime startDatetime = LocalDateTime.of(now, LocalTime.of(0, 0, 0));
LocalDateTime endDatetime = LocalDateTime.of(now, LocalTime.of(23, 59, 59));

return busRepository.findAllByDepartAtBetween(startDatetime, endDatetime);
```

## API

| Method | Path | 설명 |
| --- | --- | --- |
| `GET` | `/history?date=yyyyMMdd` | 특정 날짜의 버스 출발 히스토리를 조회합니다. |
| `GET` | `/availableTimes` | 조회 가능한 날짜 목록을 반환합니다. |
| `POST` | `/api/start` | 버스 정보 수집 작업을 시작합니다. |
| `POST` | `/api/stop` | 버스 정보 수집 작업을 중지합니다. |
| `GET` | `/api/working` | 현재 수집 작업 실행 여부를 반환합니다. |
| `GET` | `/test/allBus` | 저장된 전체 버스 데이터를 조회하는 테스트용 API입니다. |
| `GET` | `/test/insert` | 버스 데이터를 직접 추가하는 테스트용 API입니다. |

모든 API 응답은 `MessageDto`로 감싸서 반환합니다.

```json
{
  "success": true,
  "message": null,
  "result": {},
  "code": 200
}
```

## 데이터 모델

`Bus` 엔티티는 실제 출발 히스토리를 저장하기 위한 정보를 갖습니다.

| 필드 | 설명 |
| --- | --- |
| `id` | 저장 데이터 ID |
| `departAt` | 시간표 기준 출발 시각 |
| `createAt` | API를 통해 감지한 실제 수집 시각 |
| `busId` | 버스 API에서 제공하는 차량 ID |
| `busInterval` | 직전 출발 기록과의 분 단위 간격 |
| `busNum` | 차량 번호 |
| `message` | API 도착 메시지 |
| `isFirst` | 첫차 시간대 여부 |
| `isLast` | 막차 시간대 여부 |

## 로컬 실행

```bash
./gradlew bootRun
```

빌드:

```bash
./gradlew clean build
```

테스트:

```bash
./gradlew test
```

## 배포 기록

프로젝트 내부 주석 기준으로 EC2에서 다음 흐름으로 배포/실행했습니다.

```bash
./gradlew clean build -x test
nohup java -jar ./build/libs/spring_project-0.0.1-SNAPSHOT.jar &
curl --location --request POST 'http://서버주소:8080/api/start'
```

Flutter Web 빌드 결과물은 `src/main/resources/web` 아래에 포함되어 있으며, `application.yml`에서 Spring 정적 리소스 경로를 `classpath:/web/`으로 설정해 제공합니다.
