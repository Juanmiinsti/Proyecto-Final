package com.example.proyectoFinal.dto.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserDTO implements Serializable {
    private int id;
    private String name;
    private String password;

}
