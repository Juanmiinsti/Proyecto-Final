package com.example.proyectoFinal.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private LocalDateTime date;
    @Column(nullable = false)
    private double length;
    @ManyToOne
    @JoinColumn(referencedColumnName = "id",name = "charWinnerId")
    private Character charWinner;
    @ManyToOne
    @JoinColumn(referencedColumnName = "id",name = "charLoserId")
    private Character charLoser;
    @ManyToOne
    @JoinColumn(referencedColumnName = "id",name = "userWinnerId")
    private User userWinner;
    @ManyToOne
    @JoinColumn(referencedColumnName = "id",name = "userLoserId")
    private User userLoser;


}
