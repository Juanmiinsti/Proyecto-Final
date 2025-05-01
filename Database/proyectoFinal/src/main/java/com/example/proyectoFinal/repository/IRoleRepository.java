package com.example.proyectoFinal.repository;

import com.example.proyectoFinal.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IRoleRepository extends JpaRepository<Role, Integer> {
    @Query(value = "select r.id \n" +
            "from  role r\n" +
            "inner join user_role ur on r.id =ur.role_id\n" +
            "inner join user u on ur.user_id=u.id \n" +
            "where u.name =?1 ",
            nativeQuery = true)
    List<Integer>rolesbyname(String name);

}
