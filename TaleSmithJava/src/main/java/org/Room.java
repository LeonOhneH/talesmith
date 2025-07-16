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
        this.drops = drops;
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

        System.out.println("\n🔥 ══════════════════════════════════════════════════════════════");
        System.out.println("                        ⚔️ KAMPF BEGINNT! ⚔️");
        System.out.println("══════════════════════════════════════════════════════════════");
        System.out.println("   " + player.getName() + " VS " + currentEnemy.getName());
        System.out.println();

        // Kämpfer-Stats anzeigen
        System.out.println("👤 " + player.getName() + ":");
        System.out.println("   ❤️  HP: " + player.getHp() + "/" + player.getMaxHp());
        System.out.println("   ⚔️  AP: " + player.getAp());
        System.out.println("   ⚡ Speed: " + player.getAgility());

        System.out.println();

        System.out.println("👹 " + currentEnemy.getName() + ":");
        System.out.println("   ❤️  HP: " + currentEnemy.getHp() + "/" + currentEnemy.getMaxHp());
        System.out.println("   ⚔️  AP: " + currentEnemy.getAp());
        System.out.println("   ⚡ Speed: " + currentEnemy.getAgility());

        System.out.println();

        // Wer greift zuerst an bestimmen und anzeigen
        boolean playerFirst = player.getAgility() >= currentEnemy.getAgility();
        if (playerFirst) {
            if (player.getAgility() > currentEnemy.getAgility()) {
                System.out.println("⚡ " + player.getName() + " ist schneller und greift zuerst an! (Speed: "
                        + player.getAgility() + " vs " + currentEnemy.getAgility() + ")");
            } else {
                System.out.println(
                        "⚖️  Gleiche Geschwindigkeit! " + player.getName() + " greift als Spieler zuerst an! (Speed: "
                                + player.getAgility() + " vs " + currentEnemy.getAgility() + ")");
            }
        } else {
            System.out.println("⚡ " + currentEnemy.getName() + " ist schneller und greift zuerst an! (Speed: "
                    + currentEnemy.getAgility() + " vs " + player.getAgility() + ")");
        }

        System.out.println("══════════════════════════════════════════════════════════════");

        int rounds = 0;
        while (currentEnemy.isAlive() && player.isAlive() && rounds < 20) {
            rounds++;
            System.out.println("\n━━━━━━━━━━━━━━━━━━━━━ RUNDE " + rounds + " ━━━━━━━━━━━━━━━━━━━━━");

            // Geschwindigkeit bestimmt Angriffsreihenfolge
            if (playerFirst) {
                player.attack(currentEnemy);
                if (currentEnemy.isAlive()) {
                    System.out.println("   ────────────────────────────────────────");
                    currentEnemy.attack(player);
                }
            } else {
                currentEnemy.attack(player);
                if (player.isAlive()) {
                    System.out.println("   ────────────────────────────────────────");
                    player.attack(currentEnemy);
                }
            }

            if (currentEnemy.isAlive() && player.isAlive()) {
                try {
                    Thread.sleep(1500); // Etwas länger für bessere Lesbarkeit
                } catch (InterruptedException e) {
                }
            }
        }

        System.out.println("\n🔥 ══════════════════════════════════════════════════════════════");
        System.out.println("                        ⚔️ KAMPF BEENDET! ⚔️");
        System.out.println("══════════════════════════════════════════════════════════════");

        if (player.isDead()) {
            System.out.println("💀 " + player.getName() + " wurde besiegt! Das Abenteuer endet hier...");
        } else if (currentEnemy.isDead()) {
            System.out.println("🎉 " + player.getName() + " hat " + currentEnemy.getName() + " besiegt!");
            System.out.println("🏆 Sieg nach " + rounds + " Runden!");

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
            System.out.println("⏰ Der Kampf wurde nach " + rounds + " Runden beendet (Zeitlimit erreicht)!");
        }
        System.out.println("══════════════════════════════════════════════════════════════\n");
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