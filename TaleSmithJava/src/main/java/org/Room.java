package main.java.org;

import java.util.List;
import java.util.ArrayList;

public class Room {
    private String name;
    private List<Enemy> enemies;
    private boolean cleared;
    private List<Item> drops;

    public Room(String name, List<Enemy> enemies, List<Item> drops) {
        this.setName(name);
        this.setEnemies(enemies);
        this.setCleared(false);
        this.drops = drops != null ? drops : new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }

    public boolean isCleared() {
        return cleared;
    }

    public void setCleared(boolean cleared) {
        this.cleared = cleared;
    }

    public List<Item> getDrops() {
        return drops;
    }

    public void setDrops(List<Item> drops) {
        this.drops = drops;
    }

    public List<Enemy> getAliveEnemies() {
        List<Enemy> aliveEnemies = new ArrayList<>();
        for (Enemy enemy : enemies) {
            if (enemy.isAlive()) {
                aliveEnemies.add(enemy);
            }
        }
        return aliveEnemies;
    }

    public void checkCleared() {
        boolean allDead = true;
        for (Enemy enemy : enemies) {
            if (enemy.isAlive()) {
                allDead = false;
                break;
            }
        }
        this.setCleared(allDead);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("🏛️ Raum: ").append(name).append("\n");

        List<Enemy> allEnemies = getEnemies();
        List<Enemy> aliveEnemies = getAliveEnemies();

        if (!allEnemies.isEmpty()) {
            sb.append("👹 Gegner im Raum: ").append(allEnemies.size()).append("\n");
            sb.append("💚 Lebende Gegner: ").append(aliveEnemies.size()).append("\n");
            sb.append("💀 Besiegte Gegner: ").append(allEnemies.size() - aliveEnemies.size()).append("\n");

            for (Enemy enemy : allEnemies) {
                if (enemy.isAlive()) {
                    sb.append("   • ").append(enemy.toString()).append("\n");
                } else {
                    sb.append("   • 💀 ").append(enemy.getName()).append(" (BESIEGT)\n");
                }
            }
        } else {
            sb.append("✅ Keine Gegner im Raum!\n");
        }

        sb.append("🎯 Status: ").append(cleared ? "Abgeschlossen" : "In Bearbeitung");
        return sb.toString();
    }
}