package com.example.proyectoFinal.service.Object;

import com.example.proyectoFinal.Entity.Match;
import com.example.proyectoFinal.Entity.Object;
import com.example.proyectoFinal.exceptions.exceptions.CreateEntityException;
import com.example.proyectoFinal.exceptions.exceptions.NotFoundEntityException;
import com.example.proyectoFinal.repository.IObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ObjectService implements IObjectService{
    @Autowired
    private IObjectRepository objectRepository;


    @Override
    public List<Object> getObjects() {
        return objectRepository.findAll();
    }

    @Override
    public Object getObjectById(int id) {
        return objectRepository.findById(id).orElseThrow(()-> new NotFoundEntityException(String.valueOf(id), Object.class));
    }

    @Override
    public Object saveObject(Object object) {
        try {
            return objectRepository.save(object);
        }catch (Exception e){
            throw new CreateEntityException(object, e);
        }
    }

    @Override
    public void deleteObjectById(int id) {
        try {
            Object object = getObjectById(id);
            objectRepository.delete(object);
        }catch (Exception e){
            throw new NotFoundEntityException(String.valueOf(id), Object.class);
        }

    }

    @Override
    public Object updateObject(int id, Object object) {
        try {
            Object objectToUpdate = getObjectById(id);
            object.setId(objectToUpdate.getId());
            return objectRepository.save(object);
        }catch (Exception e){
            throw new CreateEntityException(object, e);
        }

    }
}
