package com.example.fightball.Models;

import java.util.List;

public class UserRolesModel {
    private int id;
    private String name;
    private String password;
    private List<RoleModel> roles;

    public UserRolesModel(int id, String name, String password, List<RoleModel> roles) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.roles = roles;
    }


    public UserRolesModel(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

