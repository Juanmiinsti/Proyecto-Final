package com.example.proyectoFinal.dto.Character;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCharacterDTO implements Serializable {
    private int id;
    private String name;
    private int max_health;
    private int max_stamina;
    private int damage;

}
