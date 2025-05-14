package com.example.proyectoFinal.service.enemy;

import com.example.proyectoFinal.Entity.Enemy;
import com.example.proyectoFinal.exceptions.exceptions.CreateEntityException;
import com.example.proyectoFinal.exceptions.exceptions.NotFoundEntityException;
import com.example.proyectoFinal.repository.IEnemyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnemyService implements IEnemyService {
    @Autowired
    private IEnemyRepository enemyRepository;

    @Override
    public List<Enemy> getEnemies() {
        return enemyRepository.findAll();
    }

    @Override
    public Enemy getEnemyById(int id) {
        return enemyRepository.findById(id).orElseThrow(() ->
                new NotFoundEntityException(String.valueOf(id), Enemy.class));
    }

    @Override
    public Enemy saveEnemy(Enemy enemy) {
        try {
            return enemyRepository.save(enemy);
        } catch (Exception e) {
            throw new CreateEntityException(enemy, e);
        }
    }

    @Override
    public void deleteEnemyById(int id) {
        try {
            Enemy enemy = getEnemyById(id);
            enemyRepository.delete(enemy);
        } catch (Exception e) {
            throw new NotFoundEntityException(String.valueOf(id), Enemy.class);
        }
    }

    @Override
    public Enemy updateEnemy(int id, Enemy enemy) {
        try {
            Enemy existing = getEnemyById(id);
            enemy.setId(existing.getId());
            return enemyRepository.save(enemy);
        } catch (Exception e) {
            throw new CreateEntityException(enemy, e);
        }
    }
}
