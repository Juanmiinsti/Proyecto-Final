package com.example.proyectoFinal.service.Role;

import com.example.proyectoFinal.Entity.Role;

import java.util.List;

public interface IRoleService {
    List<Role> getRoles();
    Role getRoleById(int id);
    Role saveRole(Role role);
    Role modifyRole(int id, Role role);
    boolean deleteRole(int id);
}
