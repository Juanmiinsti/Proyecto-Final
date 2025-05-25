package com.example.proyectoFinal.Enums;

import lombok.Getter;

@Getter
public enum RoleType {
    PLAYER(1),
    MOD(2),
    ADMIN(3);



    private int value;

    private RoleType(int value) {
        this.value = value;
    }
}
