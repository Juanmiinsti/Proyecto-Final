package com.example.proyectoFinal.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Object {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @ManyToMany
    @JoinTable(name = "use",
                joinColumns = @JoinColumn(name = "object_id",referencedColumnName = "id"),
                inverseJoinColumns = @JoinColumn(name = "character_id",referencedColumnName = "id"))
    private ArrayList<Character> characters;

}
