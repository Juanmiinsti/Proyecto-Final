package com.example.proyectoFinal.dto.Stadium;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StadiumDTO implements Serializable {

    private int id;
    private String name;
}
