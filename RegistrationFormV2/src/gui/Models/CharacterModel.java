package gui.Models;

public class CharacterModel {
    private  int id;
    private String name;
    private int max_health;
    private int max_stamina;

    public CharacterModel(int id, String name, int max_health, int max_stamina, int damage) {
        this.id = id;
        this.name = name;
        this.max_health = max_health;
        this.max_stamina = max_stamina;
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

    public int getMax_stamina() {
        return max_stamina;
    }

    public void setMax_stamina(int max_stamina) {
        this.max_stamina = max_stamina;
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

    public CharacterModel() {
    }

    private int damage;

    @Override
    public String toString() {
        return "CharacterModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", max_health=" + max_health +
                ", max_stamina=" + max_stamina +
                ", damage=" + damage +
                '}';
    }
}
