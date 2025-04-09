package com.example.proyectoFinal.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Map;
@Data
public class Response {
    private String message;
    private int code;
    private Map<String, String> errors;
    public Response( int code,String message) {
        this.message = message;
        this.code = code;
    }
    public Response(int code,String message, Map<String, String> errors) {
        this.message = message;
        this.code = code;
        this.errors = errors;
    }


    public static Response generalError(String message){
        return new Response(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
    }
    public static Response validationError( Map<String, String> errors){
        return new Response(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Validation Error", errors);
    }
    public static Response ok(String message)
    {
        return new Response(HttpStatus.OK.value(), message);
    }




}

