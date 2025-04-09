package com.example.proyectoFinal.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class Mapper {
    @Autowired
    private ModelMapper modelMapper;

    public <R> R mapType(Object source, Class<R> destinationClass) {
        return modelMapper.map(source, destinationClass);
    }

    public <S, T> List<T> mapList(List<S> sourceList, Class<T> destinationClass) {
        return sourceList.stream()
                .map(element -> modelMapper.map(element, destinationClass)).collect(Collectors.toList());
    }


}
