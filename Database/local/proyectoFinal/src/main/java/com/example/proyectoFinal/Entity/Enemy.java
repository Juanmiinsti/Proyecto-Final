package com.example.proyectoFinal.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Enemy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String name;
    @Column
    private int max_health;
    @Column
    private int damage;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "kill",
                joinColumns = @JoinColumn(name = "enemy_id",referencedColumnName = "id"),
                inverseJoinColumns = @JoinColumn(name = "character_id",referencedColumnName = "id"))
    private List<Character> characters;
}
