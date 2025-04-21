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


    @ManyToMany
    @JoinTable(name = "roles",
                joinColumns = @JoinColumn(name = "role_id",referencedColumnName = "id"),
                inverseJoinColumns = @JoinColumn(name = "user_id",referencedColumnName = "id"))
    private List<User> users;
}
