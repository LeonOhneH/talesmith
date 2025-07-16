package main.java.org.templates;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RoomTemplate {
    private String name;
    private List<EnemyTemplate> possibleEnemies;
    private HashMap<ItemTemplate, Float> possibleDrops;
    private int minEnemies;
    private int maxEnemies;

    public RoomTemplate(String name, List<EnemyTemplate> possibleEnemies, int minEnemies, int maxEnemies,
            HashMap<ItemTemplate, Float> possibleDrops) {
        this.setName(name);
        this.setPossibleEnemies(possibleEnemies);
        this.setMinEnemies(minEnemies);
        this.setMaxEnemies(maxEnemies);
        this.possibleDrops = possibleDrops;
    }

    public RoomTemplate() {
    }

    public String getName() {
        return name;
    }

    public List<EnemyTemplate> getPossibleEnemies() {
        return possibleEnemies;
    }

    public int getMinEnemies() {
        return minEnemies;
    }

    public int getMaxEnemies() {
        return maxEnemies;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPossibleEnemies(List<EnemyTemplate> possibleEnemies) {
        this.possibleEnemies = possibleEnemies;
    }

    public void setMinEnemies(int minEnemies) {
        this.minEnemies = Math.max(1, minEnemies);
    }

    public void setMaxEnemies(int maxEnemies) {
        this.maxEnemies = Math.max(this.minEnemies, maxEnemies);
    }

    public EnemyTemplate getRandomEnemyTemplate() {
        int size = this.possibleEnemies.size();
        if (size == 0) {
            throw new IllegalStateException("Keine möglichen Gegner vorhanden!");
        }

        int randomIndex = ThreadLocalRandom.current().nextInt(size);
        return this.possibleEnemies.get(randomIndex);
    }

    public int getRandomEnemyCount() {
        return ThreadLocalRandom.current().nextInt(minEnemies, maxEnemies + 1);
    }

    public int getRandomEnemyCount(int difficultyLevel) {
        int adjustedMin = Math.min(minEnemies + (difficultyLevel / 3), maxEnemies);
        int adjustedMax = Math.min(maxEnemies + (difficultyLevel / 2), maxEnemies + 3);

        return ThreadLocalRandom.current().nextInt(adjustedMin, adjustedMax + 1);
    }

    public HashMap<ItemTemplate, Float> getPossibleDrops() {
        return possibleDrops;
    }

    public void setPossibleDrops(HashMap<ItemTemplate, Float> possibleDrops) {
        this.possibleDrops = possibleDrops;
    }
}