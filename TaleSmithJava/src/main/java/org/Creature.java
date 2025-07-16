package main.java.org;

public abstract class Creature {
    private String name;
    private int hp;
    private int maxHp;
    private int ap;
    private int agility;

    public Creature(String name, int hp, int ap, int agility) {
        this.setName(name);
        this.setHp(hp);
        this.setMaxHp(hp);
        this.setAp(ap);
        this.setAgility(agility);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = Math.max(0, hp);
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = Math.max(1, maxHp);
    }

    public int getAp() {
        return ap;
    }

    public void setAp(int ap) {
        this.ap = Math.max(0, ap);
    }

    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = Math.max(0, agility);
    }

    public boolean isDead() {
        return hp <= 0;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public void heal(int amount) {
        if (amount > 0) {
            this.hp = Math.min(maxHp, hp + amount);
        }
    }

    // ENTFERNT oder DEPRECATED: Die alte attack Methode
    /*
     * public void attack(Creature target) {
     * // Diese Methode wird nicht mehr verwendet - CombatService übernimmt das
     * }
     */

    // Vereinfachte displaySimpleHealthBar für interne Nutzung (falls noch benötigt)
    // oder komplett entfernen, da Game.java seine eigene getHealthBarString hat

    @Override
    public String toString() {
        return name + " (HP: " + hp + "/" + maxHp + ", AP: " + ap + ", Agility: " + agility + ")";
    }
}