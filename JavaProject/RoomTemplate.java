import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

class RoomTemplate {
    private String name;
    private List<EnemyTemplate> possibleEnemies;

    public RoomTemplate(String name, List<EnemyTemplate> possibleEnemies) {
        this.setName(name);
        this.setPossibleEnemies(possibleEnemies);
    }

    public RoomTemplate(String name) {
        this.name = name;
    }

    public RoomTemplate() {}

    public String getName() { return name; }
    public List<EnemyTemplate> getPossibleEnemies() { return possibleEnemies; }

    public void setName(String name) {
        this.name = name;
    }

    public void setPossibleEnemies(List<EnemyTemplate> possibleEnemies) {
        this.possibleEnemies = possibleEnemies;
    }

    public EnemyTemplate getRandomEnemyTemplate() {
        int size = this.possibleEnemies.size();
        if (size == 0) {
            throw new IllegalStateException("Keine möglichen Gegner vorhanden!");
        }
        // Zufälligen Index von 0 (inklusive) bis size (exklusive) erzeugen
        int randomIndex = ThreadLocalRandom.current().nextInt(size);
        return this.possibleEnemies.get(randomIndex);
    }
}