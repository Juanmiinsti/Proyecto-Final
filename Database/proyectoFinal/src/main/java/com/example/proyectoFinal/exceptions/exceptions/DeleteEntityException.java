package com.example.proyectoFinal.exceptions.exceptions;

public class DeleteEntityException extends RuntimeException {
    public DeleteEntityException(String message) {
        super(message);
    }
    public <T> DeleteEntityException( String id,Class<T> entityClass,Throwable cause) {
        super("Entity with id " + id + " from class " + entityClass.getSimpleName() + " does not exists" , cause);
    }
}
