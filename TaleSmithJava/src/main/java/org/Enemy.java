package main.java.org;

import main.java.org.templates.EnemyTemplate;

public class Enemy extends Creature {
    private EnemyTemplate template;

    public Enemy(String name, int hp, int ap, int agility) {
        super(name, hp, ap, agility);
    }

    public Enemy(String name, int hp, int ap, int agility, EnemyTemplate template) {
        super(name, hp, ap, agility);
        this.template = template;
    }

    public EnemyTemplate getTemplate() {
        return template;
    }

    public void setTemplate(EnemyTemplate template) {
        this.template = template;
    }
}