//package com.oyc0401.spring_project.repository;
//
//import com.oyc0401.spring_project.domain.Bus;
//import org.springframework.stereotype.Repository;
//
//import java.util.*;
//
//@Repository
//public class MemoryBusRepository implements BusRepository {
//    private static Map<Long, Bus> store = new HashMap<>();
//    private static long sequence = 0L;
//
//    @Override
//    public Bus save(Bus bus) {
//        bus.setId(++sequence);
//        store.put(bus.getId(), bus);
//        return bus;
//    }
//
//    @Override
//    public Optional<Bus> findById(Long id) {
//        return Optional.ofNullable(store.get(id));
//    }
//
//    @Override
//    public Optional<Bus> findByTime(String time) {
//        return store.values().stream()
//                .filter(bus -> bus.getTime().equals(time))
//                .findAny();
//    }
//
//    @Override
//    public Optional<Bus> findByBusId(int busId) {
//        return store.values().stream()
//                .filter(bus -> bus.getBusId() == busId)
//                .findAny();
//    }
//
//
//    @Override
//    public List<Bus> findAll() {
//        return new ArrayList<>(store.values());
//    }
//
//    public void clearStore() {
//        store.clear();
//    }
//}
