package com.example.proyectoFinal.Entity;

import com.example.proyectoFinal.Enums.RoleType;
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
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)

    @Enumerated(EnumType.STRING)
    private RoleType name;


    @ManyToMany(mappedBy = "roles")
    private List<User> users;
}
