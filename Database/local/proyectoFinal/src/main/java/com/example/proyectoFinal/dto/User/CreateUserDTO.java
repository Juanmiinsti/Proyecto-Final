package com.example.proyectoFinal.dto.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class CreateUserDTO implements Serializable {
    private String name;
    private String password;
}
