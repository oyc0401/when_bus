package com.oyc0401.spring_project.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import jakarta.persistence.Entity;


public class MessageDto {
    public MessageDto(Object result) {
        this.result = result;
    }

    private boolean isSuccess = true;
    String message;
    Object result;
    int code=200;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
