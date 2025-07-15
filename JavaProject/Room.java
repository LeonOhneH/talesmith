import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

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

    public void fight(Player player, Enemy currentEnemy) {
        if (currentEnemy.isDead()) {
            System.out.println("❌ Dieser Gegner ist bereits besiegt!");
            return;
        }

        System.out.println("══════════════════════════════════════════════════════════════");
        System.out.println("KAMPF BEGINNT!");
        System.out.println("   " + player.getName() + " gegen " + currentEnemy.getName());
        System.out.println("══════════════════════════════════════════════════════════════");

        int rounds = 0;
        while (currentEnemy.isAlive() && player.isAlive() && rounds < 20) {
            rounds++;
            System.out.println("🔄 Runde " + rounds + ":");

            // Geschwindigkeit bestimmt Angriffsreihenfolge
            if (player.getAgility() >= currentEnemy.getAgility()) {
                player.attack(currentEnemy);
                if (currentEnemy.isAlive()) {
                    currentEnemy.attack(player);
                }
            } else {
                currentEnemy.attack(player);
                if (player.isAlive()) {
                    player.attack(currentEnemy);
                }
            }

            if (currentEnemy.isAlive() && player.isAlive()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }
        }

        System.out.println("⚔️ ══════════════════════════════════════════════════════════════");
        if (player.isDead()) {
            System.out.println("💀 Du wurdest besiegt! Das Abenteuer endet hier...");
        } else if (currentEnemy.isDead()) {
            System.out.println("🎉 Du hast " + currentEnemy.getName() + " besiegt!");

            int expGain = currentEnemy.getAp() + currentEnemy.getMaxHp() / 10;
            System.out.println("✨ Du erhältst " + expGain + " Erfahrungspunkte!");
            player.gainExperience(expGain);

            int healAmount = 5;
            player.heal(healAmount);
            System.out.println("💚 Du erholst dich etwas und erhältst " + healAmount + " HP!");

            checkCleared();
            if (isCleared()) {
                System.out.println("🏆 Alle Gegner in diesem Raum wurden besiegt!");
            }
        } else {
            System.out.println("⏰ Der Kampf wurde nach " + rounds + " Runden beendet!");
        }
        System.out.println("══════════════════════════════════════════════════════════════\n");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("🏛️ Raum: ").append(name).append("\n");

        List<Enemy> aliveEnemies = getAliveEnemies();
        if (!aliveEnemies.isEmpty()) {
            sb.append("👹 Lebende Gegner: ").append(aliveEnemies.size()).append("\n");
            for (Enemy enemy : aliveEnemies) {
                sb.append("   • ").append(enemy.toString()).append("\n");
            }
        } else {
            sb.append("✅ Alle Gegner besiegt!\n");
        }

        sb.append("🎯 Status: ").append(cleared ? "Abgeschlossen" : "In Bearbeitung");
        return sb.toString();
    }
}