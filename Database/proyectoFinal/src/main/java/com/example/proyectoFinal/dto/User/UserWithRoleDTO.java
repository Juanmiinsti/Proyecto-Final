package com.example.proyectoFinal.dto.User;

import com.example.proyectoFinal.dto.Role.RoleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWithRoleDTO implements Serializable {
    private int id;
    private String name;
    private String password;
    private List<RoleDTO> roles;
}
