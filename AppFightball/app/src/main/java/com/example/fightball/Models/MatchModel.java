package com.example.fightball.Models;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class MatchModel {
    private int id;
    private LocalDateTime date;
    private double length;

    private int charWinnerId;
    private int charLoserId;

    private int userWinnerId;
    private int userLoserId;
}
