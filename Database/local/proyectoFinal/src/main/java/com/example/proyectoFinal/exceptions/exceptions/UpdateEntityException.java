package com.example.proyectoFinal.exceptions.exceptions;

public class UpdateEntityException extends RuntimeException {
  public UpdateEntityException(String message) {
    super(message);

  }
  public  UpdateEntityException(Object  entity, Throwable exception) {
    super("Error al modificar Entidad: "+" "+entity.toString(),exception);
  }
}
