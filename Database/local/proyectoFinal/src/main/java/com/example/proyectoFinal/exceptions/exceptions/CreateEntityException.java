package com.example.proyectoFinal.exceptions.exceptions;

public class CreateEntityException extends RuntimeException {
    public CreateEntityException(String message) {
        super(message);
    }
    public <R> CreateEntityException(Object  entity, Throwable exception) {
        super("Something went wrong creating Entity: "+entity.getClass().getSimpleName()+" "+entity,exception);
    }
}
