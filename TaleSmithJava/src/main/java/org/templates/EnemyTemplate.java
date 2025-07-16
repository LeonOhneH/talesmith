package main.java.org.templates;

import java.util.HashMap;

public class EnemyTemplate {
    private String name;
    private int hp;
    private int ap;
    private int agility;
    private HashMap<ItemTemplate, Float> possibleDrops;

    public EnemyTemplate(String name, int hp, int ap, int agility, HashMap<ItemTemplate, Float> possibleDrops) {
        this.setName(name);
        this.setHp(hp);
        this.setAp(ap);
        this.setAgility(agility);
        this.possibleDrops = possibleDrops;
    }

    public EnemyTemplate() {
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
        this.hp = hp;
    }

    public int getAp() {
        return ap;
    }

    public void setAp(int ap) {
        this.ap = ap;
    }

    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public HashMap<ItemTemplate, Float> getPossibleDrops() {
        return possibleDrops;
    }

    public void setPossibleDrops(HashMap<ItemTemplate, Float> possibleDrops) {
        this.possibleDrops = possibleDrops;
    }
}