package main.java.org.builder;

import main.java.org.Enemy;
import main.java.org.Room;
import main.java.org.templates.EnemyTemplate;
import main.java.org.templates.RoomTemplate;

import java.util.ArrayList;
import java.util.List;

public class RoomBuilder {
    private RoomTemplate template;
    private List<Enemy> enemies;

    public RoomBuilder(RoomTemplate template, List<Enemy> enemies) {
        this.template = template;
        this.enemies = enemies;
    }

    public RoomBuilder() {
    }

    public RoomBuilder withTemplate(RoomTemplate template, int numberOfEnemies) {
        this.template = template;
        this.enemies = new ArrayList<Enemy>();
        for (int i = 0; i < numberOfEnemies; i++) {
            this.enemies.add(new EnemyBuilder().withTemplate(template.getRandomEnemyTemplate()).build());
        }
        return this;
    }

    public RoomBuilder withTemplate(RoomTemplate template, int numberOfEnemies, int difficultyLevel) {
        this.template = template;
        this.enemies = new ArrayList<>();
        for (int i = 0; i < numberOfEnemies; i++) {
            EnemyTemplate baseTemplate = template.getRandomEnemyTemplate();

            Enemy scaledEnemy = new EnemyBuilder()
                    .withTemplate(baseTemplate)
                    .scaleDifficulty(1.0 + (difficultyLevel - 1) * 0.20)
                    .build();

            this.enemies.add(scaledEnemy);
        }
        return this;
    }

    public RoomBuilder withTemplate(RoomTemplate template) {
        int numberOfEnemies = template.getRandomEnemyCount();
        return withTemplate(template, numberOfEnemies);
    }

    public RoomBuilder withTemplateAndDifficulty(RoomTemplate template, int difficultyLevel) {
        int numberOfEnemies = template.getRandomEnemyCount(difficultyLevel);
        return withTemplate(template, numberOfEnemies, difficultyLevel);
    }

    public RoomBuilder addEnemy(Enemy enemy) {
        if (this.enemies == null) {
            this.enemies = new ArrayList<>();
        }
        this.enemies.add(enemy);
        return this;
    }

    public Room build() {
        return new Room(template.getName(), enemies);
    }
}