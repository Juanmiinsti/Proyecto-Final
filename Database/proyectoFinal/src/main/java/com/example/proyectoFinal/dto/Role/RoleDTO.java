package com.example.proyectoFinal.dto.Role;

import com.example.proyectoFinal.Enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO implements Serializable {
    private int id;
    private RoleType name;
}
