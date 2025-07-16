package main.java.org.builder;

import main.java.org.Enemy;
import main.java.org.Item;
import main.java.org.Room;
import main.java.org.service.DropService;
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
    private DropService dropService;

    public RoomBuilder() {
        // defaults
        this.numberOfEnemies = 3;
        this.difficultyLevel = 1;
        this.dropService = new DropService();
        this.drops = new ArrayList<>();
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

        if (this.template != null) {
            this.numberOfEnemies = this.template.getRandomEnemyCount(this.difficultyLevel);
        }

        for (int i = 0; i < this.numberOfEnemies; i++) {
            EnemyTemplate baseTemplate = template.getRandomEnemyTemplate();

            Enemy scaledEnemy = new EnemyBuilder()
                    .withTemplate(baseTemplate)
                    .scaleDifficulty(1.0 + (this.difficultyLevel - 1) * 0.20)
                    .build();

            this.enemies.add(scaledEnemy);
        }

        // Generiere mögliche Drops für den Raum
        if (template != null && template.getPossibleDrops() != null) {
            this.drops = dropService.generateDrops(template.getPossibleDrops());
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