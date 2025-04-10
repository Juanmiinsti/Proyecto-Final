package com.example.proyectoFinal.service.Role;

import com.example.proyectoFinal.Entity.Role;
import com.example.proyectoFinal.exceptions.exceptions.CreateEntityException;
import com.example.proyectoFinal.exceptions.exceptions.NotFoundEntityException;
import com.example.proyectoFinal.repository.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService implements IRoleService {

    @Autowired
    IRoleRepository roleRepository;

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role getRoleById(int id) {
        return roleRepository.findById(id).orElseThrow(() -> new NotFoundEntityException(String.valueOf(id), Role.class));
    }

    @Override
    public Role saveRole(Role role) {
        try {
            return roleRepository.save(role);
        } catch (Exception e) {
            throw new CreateEntityException(role, e);
        }
    }

    @Override
    public Role modifyRole(int id, Role role) {
        Role oldRole = roleRepository.findById(id).orElseThrow(() -> new NotFoundEntityException(String.valueOf(id), Role.class));
        role.setId(oldRole.getId());
        return roleRepository.save(role);
    }

    @Override
    public boolean deleteRole(int id) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new NotFoundEntityException(String.valueOf(id), Role.class));
        roleRepository.delete(role);
        return true;
    }
}
