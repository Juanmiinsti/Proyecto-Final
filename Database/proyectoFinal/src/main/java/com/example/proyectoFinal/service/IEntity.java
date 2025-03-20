package com.example.proyectoFinal.service;

import java.util.List;

public interface IEntity<Class,Id> {
    List<Class> getAll();
    Class getById(Id id);
    Class modify(Class entity, Id id);
    Class save(Class entity);
    void delete(Id id);
    
}
