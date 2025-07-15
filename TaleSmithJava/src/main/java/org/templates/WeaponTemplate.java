package main.java.org.templates;

public class WeaponTemplate extends ItemTemplate{
    int damage;
    WeaponTypeE type;

    public WeaponTemplate(String name, String description, int damage, WeaponTypeE type) {
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
