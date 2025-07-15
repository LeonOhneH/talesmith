import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

class Game {
    private String name;
    private List<RoomTemplate> roomTemplates;
    private List<EnemyTemplate> enemyTemplates;
    private Player player;
    private Room currentRoom;
    private Scanner scanner;
    private Random random;
    private int roomsCleared;
    private int totalEnemiesKilled;
    private int difficultyLevel;

    public Game() {
        this.scanner = new Scanner(System.in);
        this.random = new Random();
        this.roomsCleared = 0;
        this.totalEnemiesKilled = 0;
        this.difficultyLevel = 1;
    }

    public Game(String name, List<RoomTemplate> roomTemplates, Player player) {
        this();
        this.setName(name);
        this.setRoomTemplates(roomTemplates);
        this.setPlayer(player);
    }

    private void clearScreen() {
        // ANSI escape codes für das Löschen des Bildschirms
        System.out.print("\033[2J\033[H");
        System.out.flush();
    }

    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void animatedPrint(String text, int delay) {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            System.out.flush();
            sleep(delay);
        }
    }

    private void drawBox(String title, String[] content) {
        clearScreen();

        int maxWidth = 70;
        String horizontal = "═".repeat(maxWidth - 2);

        System.out.println("╔" + horizontal + "╗");

        // Titel zentrieren
        if (title != null && !title.isEmpty()) {
            int padding = (maxWidth - 2 - title.length()) / 2;
            String titleLine = " ".repeat(padding) + title + " ".repeat(maxWidth - 2 - padding - title.length());
            System.out.println("║" + titleLine + "║");
            System.out.println("╠" + horizontal + "╣");
        }

        // Content
        for (String line : content) {
            int padding = maxWidth - 2 - line.replaceAll("[\u2600-\u27BF]", "").length();
            if (padding < 0)
                padding = 0;
            String contentLine = line + " ".repeat(padding);
            if (contentLine.length() > maxWidth - 2) {
                contentLine = contentLine.substring(0, maxWidth - 2);
            }
            System.out.println("║" + contentLine + "║");
        }

        // Fülle den Rest bis zur gewünschten Höhe (ca. 20 Zeilen)
        for (int i = content.length; i < 15; i++) {
            System.out.println("║" + " ".repeat(maxWidth - 2) + "║");
        }

        System.out.println("╚" + horizontal + "╝");
    }

    public void start() {
        displayWelcome();
        gameLoop();
    }

    private void displayWelcome() {
        String[] welcomeContent = {
                "",
                "🎮 ENDLOSES ABENTEUER! 🎮",
                "",
                "Überlebe so lange wie möglich!",
                "",
                "Spieler: " + player.getName(),
                "",
                "Bereit für das Abenteuer?",
                "",
                "Drücke Enter zum Starten..."
        };

        drawBox(name, welcomeContent);
        scanner.nextLine();
    }

    public int gameLoop() {
        while (true) {
            int result = roomLoop();
            if (result == 0) {
                roomsCleared++;

                if (roomsCleared % 2 == 0) {
                    difficultyLevel++;
                    displayDifficultyIncrease();
                }

                displayRoomCleared();
                sleep(1000);
            } else {
                displayGameOver();

                if (askForRestart()) {
                    resetGame();
                    continue;
                } else {
                    return result;
                }
            }
        }
    }

    private void displayDifficultyIncrease() {
        String[] content = {
                "",
                "⚡ SCHWIERIGKEITSGRAD ERHÖHT! ⚡",
                "",
                "🎯 Level " + difficultyLevel + " erreicht!",
                "👹 Stärkere Gegner erwarten dich!",
                "",
                "💚 Bonus-Heilung erhalten!",
                ""
        };

        drawBox("LEVEL UP!", content);

        int bonusHeal = 30 + (difficultyLevel * 10);
        player.heal(bonusHeal);

        sleep(1500);
    }

    private boolean askForRestart() {
        String[] content = {
                "",
                "💀 GAME OVER 💀",
                "",
                "🏛️ Räume überlebt: " + roomsCleared,
                "💀 Gegner besiegt: " + totalEnemiesKilled,
                "🔥 Schwierigkeit: Level " + difficultyLevel,
                "",
                "Möchtest du ein neues Spiel starten?",
                "",
                "(j/n): "
        };

        drawBox("SPIEL BEENDET", content);

        String input = scanner.nextLine().toLowerCase();
        return input.equals("j") || input.equals("ja") || input.equals("y") || input.equals("yes");
    }

    private void resetGame() {
        this.roomsCleared = 0;
        this.totalEnemiesKilled = 0;
        this.difficultyLevel = 1;

        String playerName = player.getName();
        this.player = new Player(playerName, 100, 50, 40);

        displayWelcome();
    }

    public int roomLoop() {
        generateNewRoom();
        displayRoomEntry();

        while (!currentRoom.isCleared()) {
            if (player.isDead()) {
                return 1;
            }

            displayRoomStatus();
            int choice = getValidInput(1, 4);

            switch (choice) {
                case 1:
                    handleFight();
                    break;
                case 2:
                    displayPlayerStats();
                    break;
                case 3:
                    displayGameStats();
                    break;
                case 4:
                    if (confirmQuit())
                        return 1;
                    break;
            }
        }

        return 0;
    }

    private void displayRoomEntry() {
        String[] content = {
                "",
                "🏛️ Du betrittst: " + currentRoom.getName(),
                "",
                "🔥 Schwierigkeitsgrad: Level " + difficultyLevel,
                "👹 Anzahl Gegner: " + currentRoom.getEnemies().size(),
                "",
                "Bereite dich auf den Kampf vor!",
                "",
                "Drücke Enter um fortzufahren..."
        };

        drawBox("NEUER RAUM", content);
        scanner.nextLine();
    }

    private void displayRoomStatus() {
        List<String> content = new ArrayList<>();
        content.add("");
        content.add("📍 " + currentRoom.getName() + " (Level " + difficultyLevel + ")");
        content.add("");

        // Spieler Status
        content.add("👤 " + player.getName() + " (Lv." + player.getLevel() + ")");
        content.add(getHealthBarString(player));
        content.add("");

        // Gegner Status
        List<Enemy> allEnemies = currentRoom.getEnemies();
        content.add("👹 Gegner (" + allEnemies.size() + "):");

        for (int i = 0; i < allEnemies.size() && i < 5; i++) {
            Enemy enemy = allEnemies.get(i);
            if (enemy.isAlive()) {
                content.add("  " + (i + 1) + ". " + enemy.getName() + " (AP:" + enemy.getAp() + ")");
            } else {
                content.add("  " + (i + 1) + ". 💀 " + enemy.getName() + " (BESIEGT)");
            }
        }

        content.add("");
        content.add("⚔️ Was möchtest du tun?");
        content.add("1. 🗡️ Kämpfen  2. 📊 Stats  3. 📈 Info  4. 🚪 Beenden");
        content.add("");
        content.add("Deine Wahl: ");

        drawBox("AKTUELLER RAUM", content.toArray(new String[0]));
    }

    private void handleFight() {
        List<Enemy> aliveEnemies = currentRoom.getAliveEnemies();
        if (aliveEnemies.isEmpty()) {
            String[] content = {
                    "",
                    "🎉 Alle Gegner wurden bereits besiegt!",
                    "",
                    "Drücke Enter um fortzufahren..."
            };
            drawBox("KAMPF", content);
            scanner.nextLine();
            return;
        }

        // Gegner auswählen
        List<String> content = new ArrayList<>();
        content.add("");
        content.add("⚔️ Welchen Gegner möchtest du angreifen?");
        content.add("");

        for (int i = 0; i < aliveEnemies.size(); i++) {
            Enemy enemy = aliveEnemies.get(i);
            content.add((i + 1) + ". " + enemy.getName() + " (HP:" + enemy.getHp() + " AP:" + enemy.getAp() + ")");
        }

        content.add("");
        content.add("Deine Wahl: ");

        drawBox("GEGNER WÄHLEN", content.toArray(new String[0]));

        int choice = getValidInput(1, aliveEnemies.size());
        Enemy targetEnemy = aliveEnemies.get(choice - 1);

        // Animierter Kampf
        animatedFight(player, targetEnemy);

        if (targetEnemy.isDead()) {
            totalEnemiesKilled++;
        }
    }

    private void animatedFight(Player player, Enemy enemy) {
        clearScreen();

        // Kampf-Header
        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║                        ⚔️ KAMPF BEGINNT! ⚔️                      ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════╣");
        System.out.printf("║  %s VS %s%n",
                String.format("%-30s", player.getName()),
                String.format("%30s", enemy.getName()));
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");

        sleep(750);

        // Kämpfer-Stats animiert anzeigen
        animatedPrint("\n👤 " + player.getName() + ":\n", 25);
        sleep(150);
        animatedPrint("   ❤️ HP: " + player.getHp() + "/" + player.getMaxHp() + "\n", 15);
        sleep(150);
        animatedPrint("   ⚔️ AP: " + player.getAp() + "\n", 15);
        sleep(150);
        animatedPrint("   ⚡ Speed: " + player.getAgility() + "\n", 15);

        sleep(400);

        animatedPrint("\n👹 " + enemy.getName() + ":\n", 25);
        sleep(150);
        animatedPrint("   ❤️ HP: " + enemy.getHp() + "/" + enemy.getMaxHp() + "\n", 15);
        sleep(150);
        animatedPrint("   ⚔️ AP: " + enemy.getAp() + "\n", 15);
        sleep(150);
        animatedPrint("   ⚡ Speed: " + enemy.getAgility() + "\n", 15);

        sleep(750);

        // Geschwindigkeit bestimmen
        boolean playerFirst = player.getAgility() >= enemy.getAgility();
        if (playerFirst) {
            animatedPrint("\n⚡ " + player.getName() + " ist schneller und greift zuerst an!\n", 20);
        } else {
            animatedPrint("\n⚡ " + enemy.getName() + " ist schneller und greift zuerst an!\n", 20);
        }

        sleep(1000);

        // Kampf-Schleife
        int rounds = 0;
        while (enemy.isAlive() && player.isAlive() && rounds < 20) {
            rounds++;

            animatedPrint("\n━━━━━━━━━━━━━━━━━━━━━ RUNDE " + rounds + " ━━━━━━━━━━━━━━━━━━━━━\n", 15);
            sleep(500);

            if (playerFirst) {
                animatedAttack(player, enemy);
                if (enemy.isAlive()) {
                    sleep(750);
                    animatedAttack(enemy, player);
                }
            } else {
                animatedAttack(enemy, player);
                if (player.isAlive()) {
                    sleep(750);
                    animatedAttack(player, enemy);
                }
            }

            if (enemy.isAlive() && player.isAlive()) {
                sleep(1000);
            }
        }

        // Kampf-Ende
        sleep(500);
        animatedPrint("\n🔥 ══════════════ KAMPF BEENDET! ══════════════\n", 25);
        sleep(500);

        if (player.isDead()) {
            animatedPrint("💀 " + player.getName() + " wurde besiegt!\n", 25);
        } else if (enemy.isDead()) {
            animatedPrint("🎉 " + player.getName() + " hat gewonnen!\n", 25);
            sleep(500);

            int expGain = enemy.getAp() + enemy.getMaxHp() / 10;
            animatedPrint("✨ Du erhältst " + expGain + " Erfahrungspunkte!\n", 15);
            player.gainExperience(expGain);

            sleep(500);
            int healAmount = 5;
            player.heal(healAmount);
            animatedPrint("💚 Du erholst dich: +" + healAmount + " HP!\n", 15);

            currentRoom.checkCleared();
        }

        sleep(1000);
        animatedPrint("\nDrücke Enter um fortzufahren...", 15);
        scanner.nextLine();
    }

    private void animatedAttack(Creature attacker, Creature target) {
        animatedPrint("⚔️ " + attacker.getName() + " greift " + target.getName() + " an!\n", 20);
        sleep(400);

        // Angriffs-Animation
        animatedPrint("   💪 Angriffskraft: " + attacker.getAp() + "\n", 15);
        sleep(250);

        boolean criticalHit = Math.random() < 0.1;
        int damage = criticalHit ? (int) (attacker.getAp() * 1.5) : attacker.getAp();

        if (criticalHit) {
            animatedPrint("   💥 KRITISCHER TREFFER! ", 25);
            sleep(250);
            animatedPrint("Schaden: " + damage + "\n", 20);
        } else {
            animatedPrint("   🗡️ Schaden verursacht: " + damage + "\n", 20);
        }

        target.setHp(target.getHp() - damage);
        sleep(300);

        // Health Bar nach Angriff
        animatedPrint("   " + getHealthBarString(target) + "\n", 10);

        if (target.isDead()) {
            sleep(400);
            animatedPrint("   💀 " + target.getName() + " wurde besiegt!\n", 25);
        }

        sleep(250);
    }

    private String getHealthBarString(Creature creature) {
        int maxHp = creature.getMaxHp();
        int currentHp = creature.getHp();
        double healthPercent = (double) currentHp / maxHp;

        String healthIcon;
        if (healthPercent > 0.75) {
            healthIcon = "❤️";
        } else if (healthPercent > 0.5) {
            healthIcon = "🧡";
        } else if (healthPercent > 0.25) {
            healthIcon = "💛";
        } else if (healthPercent > 0) {
            healthIcon = "💔";
        } else {
            healthIcon = "💀";
        }

        int barWidth = 15;
        int filledBars = (int) (healthPercent * barWidth);
        StringBuilder healthBar = new StringBuilder();

        healthBar.append(healthIcon).append(" ");

        for (int i = 0; i < filledBars; i++) {
            healthBar.append("█");
        }

        for (int i = filledBars; i < barWidth; i++) {
            healthBar.append("░");
        }

        healthBar.append(" ").append(currentHp).append("/").append(maxHp);

        return healthBar.toString();
    }

    private void displayPlayerStats() {
        String[] content = {
                "",
                "👤 " + player.getName(),
                "",
                getHealthBarString(player),
                "",
                "⚔️ Angriffskraft: " + player.getAp(),
                "⚡ Geschwindigkeit: " + player.getAgility(),
                "🎯 Level: " + player.getLevel(),
                "✨ Erfahrung: " + player.getExperience(),
                "",
                "Drücke Enter um zurückzukehren..."
        };

        drawBox("CHARAKTERWERTE", content);
        scanner.nextLine();
    }

    private void displayGameStats() {
        String[] content = {
                "",
                "🏛️ Räume abgeschlossen: " + roomsCleared,
                "💀 Gegner besiegt: " + totalEnemiesKilled,
                "🔥 Schwierigkeitsgrad: Level " + difficultyLevel,
                "🏆 Spieler Level: " + player.getLevel(),
                "",
                "Drücke Enter um zurückzukehren..."
        };

        drawBox("SPIELSTATISTIKEN", content);
        scanner.nextLine();
    }

    private void displayRoomCleared() {
        String[] content = {
                "",
                "✨ RAUM ERFOLGREICH ABGESCHLOSSEN! ✨",
                "",
                "🏛️ Raum #" + (roomsCleared + 1) + " gemeistert!",
                "",
                "💚 Du erholst dich...",
                "",
                "Bereit für den nächsten Raum?"
        };

        drawBox("SIEG!", content);

        int healAmount = Math.max(10, 25 - difficultyLevel);
        player.heal(healAmount);
    }

    private void displayGameOver() {
        String rating = "";
        if (roomsCleared >= 50)
            rating = "🌟 LEGENDÄR! 🌟";
        else if (roomsCleared >= 30)
            rating = "💎 MEISTERHAFT! 💎";
        else if (roomsCleared >= 20)
            rating = "🏆 HERVORRAGEND! 🏆";
        else if (roomsCleared >= 10)
            rating = "⭐ GUT! ⭐";
        else if (roomsCleared >= 5)
            rating = "👍 ORDENTLICH! 👍";
        else
            rating = "🎯 Übung macht den Meister!";

        String[] content = {
                "",
                "💀 GAME OVER 💀",
                "",
                "📊 ENDSTATISTIKEN:",
                "🏛️ Räume überlebt: " + roomsCleared,
                "💀 Gegner besiegt: " + totalEnemiesKilled,
                "🔥 Schwierigkeit: Level " + difficultyLevel,
                "🏆 Spieler-Level: " + player.getLevel(),
                "",
                "🎖️ " + rating
        };

        drawBox("SPIEL BEENDET", content);
    }

    private boolean confirmQuit() {
        String[] content = {
                "",
                "❓ Möchtest du wirklich das Spiel beenden?",
                "",
                "(j/n): "
        };

        drawBox("SPIEL BEENDEN?", content);

        String input = scanner.nextLine().toLowerCase();
        return input.equals("j") || input.equals("ja");
    }

    private int getValidInput(int min, int max) {
        while (true) {
            try {
                int input = Integer.parseInt(scanner.nextLine());
                if (input >= min && input <= max) {
                    return input;
                }
                System.out.print("❌ Ungültige Eingabe (" + min + "-" + max + "): ");
            } catch (NumberFormatException e) {
                System.out.print("❌ Bitte gib eine Zahl ein: ");
            }
        }
    }

    public void generateNewRoom() {
        RoomTemplate template = roomTemplates.get(random.nextInt(roomTemplates.size()));
        this.setCurrentRoom(new RoomBuilder().withTemplateAndDifficulty(template, difficultyLevel).build());
    }

    // ...existing code... (getter und setter bleiben unverändert)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RoomTemplate> getRoomTemplates() {
        return roomTemplates;
    }

    public void setRoomTemplates(List<RoomTemplate> roomTemplates) {
        this.roomTemplates = roomTemplates;
    }

    public List<EnemyTemplate> getEnemyTemplates() {
        return enemyTemplates;
    }

    public void setEnemyTemplates(List<EnemyTemplate> enemyTemplates) {
        this.enemyTemplates = enemyTemplates;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }
}