package com.example.proyectoFinal.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class Room {
    @Id
    private Long id;

    @Column
    private String name;
    @Column
    private int capacity;

}
