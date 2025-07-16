package main.java.org.builder;

import main.java.org.Enemy;
import main.java.org.templates.EnemyTemplate;

public class EnemyBuilder {
    private String name;
    private int hp;
    private int ap;
    private int agility;
    private EnemyTemplate template;

    public EnemyBuilder(EnemyTemplate template) {
        this.name = template.getName();
        this.hp = template.getHp();
        this.ap = template.getAp();
        this.agility = template.getAgility();
        this.template = template;
    }

    public EnemyBuilder() {
    }

    public EnemyBuilder withTemplate(EnemyTemplate template) {
        this.name = template.getName();
        this.hp = template.getHp();
        this.ap = template.getAp();
        this.agility = template.getAgility();
        this.template = template;
        return this;
    }

    public EnemyBuilder scaleDifficulty(double factor) {
        this.hp = (int) (this.hp * factor);
        this.ap = (int) (this.ap * factor);
        this.agility = (int) (this.agility * factor);
        return this;
    }

    public Enemy build() {
        return new Enemy(name, hp, ap, agility, template);
    }
}
