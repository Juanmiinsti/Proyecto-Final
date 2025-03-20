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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "userWinner")
    private ArrayList<Match> winMatches;

    @OneToMany(mappedBy = "userLoser")
    private ArrayList<Match> lossMatches;

    @ManyToMany(mappedBy = "roles")
    private ArrayList<Role> roles;

}
