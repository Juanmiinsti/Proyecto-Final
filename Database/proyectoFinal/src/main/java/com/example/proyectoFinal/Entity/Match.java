package com.example.proyectoFinal.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @JoinColumn(referencedColumnName = "id",name = "char_Winner_Id" )
    @OnDelete(action = OnDeleteAction.SET_NULL) // o SET_NULL si prefieres
    private Character charWinner;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id",name = "char_Loser_Id")
    @OnDelete(action = OnDeleteAction.SET_NULL) // o SET_NULL si prefieres
    private Character charLoser;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id",name = "user_Winner_Id")
    private User userWinner;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id",name = "user_Loser_Id")
    private User userLoser;


}
