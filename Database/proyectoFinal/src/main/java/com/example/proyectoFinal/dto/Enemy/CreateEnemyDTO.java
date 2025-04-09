package com.example.proyectoFinal.dto.Enemy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateEnemyDTO implements Serializable {
    private String name;
    private int max_health;
    private int damage;
}
