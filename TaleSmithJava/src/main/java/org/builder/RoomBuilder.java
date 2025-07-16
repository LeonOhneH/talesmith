package main.java.org.builder;

import main.java.org.Enemy;
import main.java.org.Item;
import main.java.org.Room;
import main.java.org.templates.EnemyTemplate;
import main.java.org.templates.RoomTemplate;

import java.util.ArrayList;
import java.util.List;

public class RoomBuilder {
    private RoomTemplate template;
    private List<Enemy> enemies;
    private List<Item> drops;
    int numberOfEnemies;
    int difficultyLevel;

    public RoomBuilder() {
        // defaults
        this.numberOfEnemies = 3;
        this.difficultyLevel = 1;
    }

    public RoomBuilder withTemplate(RoomTemplate template) {
        this.template = template;
        return this;
    }

    public RoomBuilder withNumberOfEnemies(int numberOfEnemies) {
        this.numberOfEnemies = numberOfEnemies;
        return this;
    }

    public RoomBuilder withDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
        return this;
    }

    public RoomBuilder generateEnemies() {
        this.enemies = new ArrayList<>();

        for (int i = 0; i < this.numberOfEnemies; i++) {
            EnemyTemplate baseTemplate = template.getRandomEnemyTemplate();

            Enemy scaledEnemy = new EnemyBuilder()
                    .withTemplate(baseTemplate)
                    .scaleDifficulty(1.0 + (this.difficultyLevel - 1) * 0.20)
                    .build();

            this.enemies.add(scaledEnemy);
        }
        return this;
    }

    public RoomBuilder addEnemy(Enemy enemy) {
        if (this.enemies == null) {
            this.enemies = new ArrayList<>();
        }
        this.enemies.add(enemy);
        return this;
    }

    public Room build() {
        return new Room(template.getName(), enemies, drops);
    }
}