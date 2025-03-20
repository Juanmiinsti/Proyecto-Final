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
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String name;
    @ManyToMany
    @JoinTable(name = "roles",
                joinColumns = @JoinColumn(name = "role_id",referencedColumnName = "id"),
                inverseJoinColumns = @JoinColumn(name = "user_id",referencedColumnName = "id"))
    private ArrayList<User> users;
}
