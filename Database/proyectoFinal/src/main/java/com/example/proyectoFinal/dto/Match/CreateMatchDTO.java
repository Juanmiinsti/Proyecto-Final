package com.example.proyectoFinal.dto.Match;


import com.example.proyectoFinal.Entity.Character;
import com.example.proyectoFinal.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateMatchDTO implements Serializable {
    private LocalDateTime date;
    private double length;

    private int charWinner;
    private int charLoser;

    private int userWinner;
    private int userLoser;



}
