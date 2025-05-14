package com.example.fightball.Models;

public class EnemyModel {
    private int id;

    private String name;
    private int max_health;
    private int damage;
    public EnemyModel(int id, String name, int max_health, int damage) {
        this.id = id;
        this.name = name;
        this.max_health = max_health;
        this.damage = damage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMax_health() {
        return max_health;
    }

    public void setMax_health(int max_health) {
        this.max_health = max_health;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
