package com.example.proyectoFinal.exceptions.exceptions;

public class NotFoundEntityException extends RuntimeException {
    public NotFoundEntityException(String message) {
        super(message);
    }
    public <T> NotFoundEntityException( String id,Class<T> entityClass) {
        super("La entidad con id " + id + " de la clase " + entityClass.getSimpleName() + " no Existe");
    }
}
