package com.example.fightball.Models;

import java.util.Map;

public class RegisterResponse {
    private String message;
    private int code;
    private Map<String, String> errors;

    public RegisterResponse(String message, int code, Map<String, String> errors) {
        this.message = message;
        this.code = code;
        this.errors = errors;

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
