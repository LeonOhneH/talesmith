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

    public Game(String name, List<RoomTemplate> roomTemplates, List<EnemyTemplate> enemyTemplates, Player player) {
        this();
        this.setName(name);
        this.setRoomTemplates(roomTemplates);
        this.setEnemyTemplates(enemyTemplates);
        this.setPlayer(player);
    }

    public void start() {
        displayWelcome();
        gameLoop();
    }

    private void displayWelcome() {
        int minContentWidth = 60;
        String welcome = "🎮 ENDLOSES STAR WARS ABENTEUER! 🎮";
        String subtitle = "Überlebe so lange wie möglich!";

        int contentWidth = Math.max(minContentWidth,
                Math.max(name.length(), Math.max(welcome.length(), subtitle.length())));

        String horizontal = "═".repeat(contentWidth);
        String emptyLine = " ".repeat(contentWidth);

        System.out.println("╔" + horizontal + "╗");

        printCenteredLine(name, contentWidth);
        System.out.println("║" + emptyLine + "║");
        printCenteredLine(welcome, contentWidth);
        printCenteredLine(subtitle, contentWidth);
        System.out.println("║" + emptyLine + "║");

        System.out.println("╚" + horizontal + "╝");
        System.out.println();
    }

    private void printCenteredLine(String text, int width) {
        int paddingLeft = (width - text.length()) / 2;
        int paddingRight = width - text.length() - paddingLeft;
        String left = " ".repeat(paddingLeft);
        String right = " ".repeat(paddingRight);
        System.out.println("║" + left + text + right + "║");
    }

    public int gameLoop() {
        while (true) {
            int result = roomLoop();
            if (result == 0) {
                roomsCleared++;

                // Alle 2 Räume wird die Schwierigkeit erhöht
                if (roomsCleared % 2 == 0) {
                    difficultyLevel++;
                    displayDifficultyIncrease();
                }

                System.out.println("🚪 Du gehst zum nächsten Raum...\n");
                pause();
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
        System.out.println("\n🔥 ══════════════════════════════════════════════════════════════");
        System.out.println("   ⚡ SCHWIERIGKEITSGRAD ERHÖHT! ⚡");
        System.out.println("   🎯 Level " + difficultyLevel + " erreicht!");
        System.out.println("   👹 Mehr und stärkere Gegner erwarten dich!");
        System.out.println("══════════════════════════════════════════════════════════════");

        int bonusHeal = 30 + (difficultyLevel * 10);
        player.heal(bonusHeal);
        System.out.println("💚 Bonus-Heilung: +" + bonusHeal + " HP!");
        System.out.println("❤️  Aktuelle HP: " + player.getHp() + "/" + player.getMaxHp());
        System.out.println();
        pause();
    }

    private boolean askForRestart() {
        System.out.println("\n🔄 ══════════════════════════════════════════════════════════════");
        System.out.println("   Möchtest du ein neues Spiel starten?");
        System.out.println("══════════════════════════════════════════════════════════════");
        System.out.print("❓ Neues Spiel? (j/n): ");
        String input = scanner.nextLine().toLowerCase();
        return input.equals("j") || input.equals("ja") || input.equals("y") || input.equals("yes");
    }

    private void resetGame() {
        System.out.println("\n🔄 Neues Spiel wird gestartet...\n");

        // Statistiken zurücksetzen
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
            displayMenu();

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

        displayRoomCleared();
        return 0;
    }

    private void displayRoomEntry() {
        System.out.println("🏛️ ══════════════════════════════════════════════════════════════");
        System.out.println("   Du betrittst: " + currentRoom.getName());
        System.out.println("   🔥 Schwierigkeitsgrad: Level " + difficultyLevel);
        System.out.println("   👹 Anzahl Gegner: " + currentRoom.getEnemies().size());
        System.out.println("══════════════════════════════════════════════════════════════");
        System.out.println();
    }

    private void displayRoomStatus() {
        System.out.println("📍 Aktueller Raum: " + currentRoom.getName() + " (Level " + difficultyLevel + ")");
        System.out.println("❤️  Deine HP: " + player.getHp() + "/" + player.getMaxHp());

        List<Enemy> aliveEnemies = currentRoom.getAliveEnemies();
        if (!aliveEnemies.isEmpty()) {
            System.out.println("\n👹 Lebende Gegner (" + aliveEnemies.size() + "):");
            for (int i = 0; i < aliveEnemies.size(); i++) {
                Enemy enemy = aliveEnemies.get(i);
                System.out.println("   " + (i + 1) + ". " + enemy.getName() +
                        " (HP: " + enemy.getHp() + ", AP: " + enemy.getAp() + ")");
            }
        }
        System.out.println();
    }

    private void displayMenu() {
        System.out.println("⚔️  Was möchtest du tun?");
        System.out.println("1. 🗡️  Kämpfen");
        System.out.println("2. 📊 Charakterwerte anzeigen");
        System.out.println("3. 📈 Spielstatistiken anzeigen");
        System.out.println("4. 🚪 Spiel beenden");
        System.out.print("Deine Wahl: ");
    }

    private void handleFight() {
        List<Enemy> aliveEnemies = currentRoom.getAliveEnemies();
        if (aliveEnemies.isEmpty()) {
            System.out.println("🎉 Alle Gegner wurden bereits besiegt!");
            return;
        }

        System.out.println("\n⚔️  Welchen Gegner möchtest du angreifen?");
        for (int i = 0; i < aliveEnemies.size(); i++) {
            Enemy enemy = aliveEnemies.get(i);
            System.out.println((i + 1) + ". " + enemy.getName() +
                    " (HP: " + enemy.getHp() + ")");
        }

        int choice = getValidInput(1, aliveEnemies.size());
        Enemy targetEnemy = aliveEnemies.get(choice - 1);

        currentRoom.fight(player, targetEnemy);

        if (targetEnemy.isDead()) {
            totalEnemiesKilled++;
        }
    }

    private void displayPlayerStats() {
        System.out.println("\n📊 ═══ CHARAKTERWERTE ═══");
        System.out.println("👤 Name: " + player.getName());
        System.out.println("❤️  HP: " + player.getHp() + "/" + player.getMaxHp());
        System.out.println("⚔️  Angriffskraft: " + player.getAp());
        System.out.println("⚡ Geschwindigkeit: " + player.getAgility());
        System.out.println("🎯 Level: " + player.getLevel());
        System.out.println("✨ Erfahrung: " + player.getExperience());
        System.out.println("═══════════════════════════\n");
    }

    private void displayGameStats() {
        System.out.println("\n📈 ═══ SPIELSTATISTIKEN ═══");
        System.out.println("🏛️  Räume abgeschlossen: " + roomsCleared);
        System.out.println("💀 Gegner besiegt: " + totalEnemiesKilled);
        System.out.println("🔥 Schwierigkeitsgrad: Level " + difficultyLevel);
        System.out.println("🏆 Höchster Raum: " + roomsCleared);
        System.out.println("═══════════════════════════\n");
    }

    private void displayRoomCleared() {
        System.out.println("\n🎉 ══════════════════════════════════════════════════════════════");
        System.out.println("   ✨ RAUM ERFOLGREICH ABGESCHLOSSEN! ✨");
        System.out.println("   🏛️  Raum #" + (roomsCleared + 1) + " gemeistert!");
        System.out.println("══════════════════════════════════════════════════════════════");

        int healAmount = Math.max(10, 25 - difficultyLevel);
        player.heal(healAmount);
        System.out.println("💚 Du erholst dich und erhältst " + healAmount + " HP zurück!");
        System.out.println("❤️  Aktuelle HP: " + player.getHp() + "/" + player.getMaxHp());
        System.out.println();
    }

    private void displayGameOver() {
        System.out.println("\n💀 ══════════════════════════════════════════════════════════════");
        System.out.println("   ⚰️  GAME OVER ⚰️");
        System.out.println("   Dein Abenteuer endet hier...");
        System.out.println("══════════════════════════════════════════════════════════════");
        System.out.println("📊 ENDSTATISTIKEN:");
        System.out.println("🏛️  Räume überlebt: " + roomsCleared);
        System.out.println("💀 Gegner besiegt: " + totalEnemiesKilled);
        System.out.println("🔥 Erreichte Schwierigkeit: Level " + difficultyLevel);
        System.out.println("🏆 Finales Spieler-Level: " + player.getLevel());

        // Leistungsbewertung
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

        System.out.println("🎖️  Bewertung: " + rating);
        System.out.println("══════════════════════════════════════════════════════════════");
    }

    private boolean confirmQuit() {
        System.out.print("\n❓ Möchtest du wirklich das Spiel beenden? (j/n): ");
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
                System.out.print("❌ Ungültige Eingabe. Bitte wähle zwischen " + min + " und " + max + ": ");
            } catch (NumberFormatException e) {
                System.out.print("❌ Bitte gib eine gültige Zahl ein: ");
            }
        }
    }

    private void pause() {
        System.out.print("👆 Drücke Enter um fortzufahren...");
        scanner.nextLine();
        System.out.println();
    }

    public void generateNewRoom() {
        RoomTemplate template = roomTemplates.get(random.nextInt(roomTemplates.size()));

        this.setCurrentRoom(new RoomBuilder().withTemplateAndDifficulty(template, difficultyLevel).build());
    }

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