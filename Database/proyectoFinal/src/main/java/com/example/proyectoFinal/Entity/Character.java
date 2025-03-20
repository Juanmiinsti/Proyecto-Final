package com.example.proyectoFinal.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Character {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private int max_health;
    @Column(nullable = false)
    private int max_stamina;
    @Column(nullable = false)
    private int damage;

    @OneToOne(mappedBy = "character")
    private Stadium stadium;

    @ManyToMany(mappedBy = "characters")
    private ArrayList<Object> objects;

    @ManyToMany(mappedBy = "characters")
    private ArrayList<Enemy> enemies;

    @OneToMany(mappedBy = "charWinner")
    private ArrayList<Match> winMatches;

    @OneToMany(mappedBy = "charLoser")
    private ArrayList<Match> lostMatches;

}
