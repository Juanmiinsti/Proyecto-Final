package com.example.proyectoFinal.dto.Object;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateObjectDTO implements Serializable {
    private String name;
    private String description;
    private int quantity;
}
