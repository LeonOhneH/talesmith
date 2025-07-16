package main.java.org;

import main.java.org.templates.WeaponTypeE;

public class Weapon extends Item {
    int damage;
    WeaponTypeE type;

    public Weapon(String name, String description, int damage, WeaponTypeE type) {
        super(name, description);
        this.damage = damage;
        this.type = type;
    }

    public int getDamage() {
        return this.damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public WeaponTypeE getType() {
        return this.type;
    }

    public void setType(WeaponTypeE type) {
        this.type = type;
    }
}
