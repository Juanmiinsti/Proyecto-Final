package com.example.proyectoFinal.dto.Tutorial;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTutorialDTO implements Serializable {
    private String title;
    private String description;
}
