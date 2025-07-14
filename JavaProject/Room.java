import java.util.List;


class Room {
    private String name;
    private List<Enemy> enemies;
    private boolean cleared;

    public Room(String name, List<Enemy> enemies) {
        this.setName(name);
        this.setEnemies(enemies);
        this.setCleared(false);
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Raum: ").append(name).append("\n");
        sb.append("Enemies:\n");
        if (enemies != null && !enemies.isEmpty()) {
            for (Enemy e : enemies) {
                sb.append("  - ").append(e).append("\n");
            }
        } else {
            sb.append("  (keine Gegner)\n");
        }
        sb.append("Cleared: ").append(cleared);
        return sb.toString();
    }
}