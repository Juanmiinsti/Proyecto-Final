package com.example.proyectoFinal.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @JsonIgnore
    @OneToOne(mappedBy = "character")
    private Stadium stadium;

    @JsonIgnore
    @ManyToMany(mappedBy = "characters")
    private List<Object> objects;

    @JsonIgnore
    @ManyToMany(mappedBy = "characters",fetch = FetchType.EAGER)
    private List<Enemy> enemies;

    @JsonIgnore
    @OneToMany(mappedBy = "charWinner")
    private List<Match> winMatches;
    @JsonIgnore
    @OneToMany(mappedBy = "charLoser")
    private List<Match> lostMatches;

}
