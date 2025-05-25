package com.example.proyectoFinal.dto.Tutorial;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TutorialDTO implements Serializable {
    private int id;
    private String title;
    private String description;
}
