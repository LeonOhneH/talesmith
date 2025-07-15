import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

class RoomTemplate {
    private String name;
    private List<EnemyTemplate> possibleEnemies;
    private int minEnemies;
    private int maxEnemies;

    public RoomTemplate(String name, List<EnemyTemplate> possibleEnemies, int minEnemies, int maxEnemies) {
        this.setName(name);
        this.setPossibleEnemies(possibleEnemies);
        this.setMinEnemies(minEnemies);
        this.setMaxEnemies(maxEnemies);
    }

    public RoomTemplate(String name, List<EnemyTemplate> possibleEnemies) {
        this(name, possibleEnemies, 1, 3); // Standard: 1-3 Gegner
    }

    public RoomTemplate(String name) {
        this.name = name;
        this.minEnemies = 1;
        this.maxEnemies = 3;
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
        // Schwierigkeitsgrad erhöht die Anzahl der Gegner
        int adjustedMin = Math.min(minEnemies + (difficultyLevel / 3), maxEnemies);
        int adjustedMax = Math.min(maxEnemies + (difficultyLevel / 2), maxEnemies + 3);

        return ThreadLocalRandom.current().nextInt(adjustedMin, adjustedMax + 1);
    }
}