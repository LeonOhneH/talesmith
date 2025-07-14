import java.util.ArrayList;
import java.util.List;

class RoomBuilder {
    private RoomTemplate template;
    private List<Enemy> enemies;

    public RoomBuilder(RoomTemplate template, List<Enemy> enemies) {
        this.template = template;
        this.enemies = enemies;
    }

    public RoomBuilder() {}

    public RoomBuilder withTemplate(RoomTemplate template, int numberOfEnemies) {
        this.template = template;
        this.enemies = new ArrayList<>();
        for (int i = 0; i < numberOfEnemies; i++) {
            this.enemies.add(new EnemyBuilder().withTemplate(template.getRandomEnemyTemplate()).build());
        }
        return this;
    }

    public RoomBuilder addEnemy(Enemy enemy) {
        this.enemies.add(enemy);
        return this;
    }

    public Room build() {
        return new Room(template.getName(), enemies);
    }
}