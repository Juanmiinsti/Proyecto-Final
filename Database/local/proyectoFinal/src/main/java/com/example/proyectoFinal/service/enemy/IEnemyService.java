package com.example.proyectoFinal.service.enemy;

import com.example.proyectoFinal.Entity.Enemy;
import java.util.List;

public interface IEnemyService {
    List<Enemy> getEnemies();
    Enemy getEnemyById(int id);
    Enemy saveEnemy(Enemy enemy);
    void deleteEnemyById(int id);
    Enemy updateEnemy(int id, Enemy enemy);
}
