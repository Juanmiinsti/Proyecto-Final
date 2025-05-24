package com.example.proyectoFinal.dto.Match;

import com.example.proyectoFinal.Entity.Character;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchDTO implements Serializable {
    private int id;
    private LocalDateTime date;
    private double length;

    private int charWinnerId;
    private int charLoserId;

    private int userWinnerId;
    private int userLoserId;


}
