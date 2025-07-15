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

    public Game() {
        this.scanner = new Scanner(System.in);
        this.random = new Random();
        this.roomsCleared = 0;
        this.totalEnemiesKilled = 0;
    }

    public Game(String name, List<RoomTemplate> roomTemplates, List<EnemyTemplate> enemyTemplates) {
        this();
        this.setName(name);
        this.setRoomTemplates(roomTemplates);
        this.setEnemyTemplates(enemyTemplates);
    }

    public void start() {
        displayWelcome();
        createPlayer();
        gameLoop();
    }

    private void displayWelcome() {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                        " + name + "                          ║");
        System.out.println("║                                                              ║");
        System.out.println("║              Willkommen zum großen Abenteuer!                ║");
        System.out.println("║                                                              ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
    }

    private void createPlayer() {
        System.out.print("📝 Gib deinen Heldennamen ein: ");
        String playerName = scanner.nextLine();

        System.out.println("\n🎲 Wähle deine Charakterklasse:");
        System.out.println("1. 🗡️  Krieger (HP: 120, AP: 12, Agility: 8)");
        System.out.println("2. 🏹 Bogenschütze (HP: 80, AP: 15, Agility: 12)");
        System.out.println("3. ⚡ Magier (HP: 60, AP: 18, Agility: 10)");

        int choice = getValidInput(1, 3);

        switch (choice) {
            case 1:
                setPlayer(new Player(playerName, 120, 12, 8));
                System.out.println("\n⚔️ Du bist nun ein mutiger Krieger!");
                break;
            case 2:
                setPlayer(new Player(playerName, 80, 15, 12));
                System.out.println("\n🏹 Du bist nun ein geschickter Bogenschütze!");
                break;
            case 3:
                setPlayer(new Player(playerName, 60, 18, 10));
                System.out.println("\n⚡ Du bist nun ein mächtiger Magier!");
                break;
        }

        System.out.println("🎯 " + player.getName() + " beginnt das Abenteuer!\n");
        pause();
    }

    public int gameLoop() {
        while (true) {
            int result = roomLoop();
            if (result == 0) {
                roomsCleared++;
                if (roomsCleared >= 5) {
                    displayVictory();
                    return 0;
                }
                System.out.println("🚪 Du gehst zum nächsten Raum...\n");
                pause();
            } else {
                displayGameOver();
                return result;
            }
        }
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
        System.out.println("══════════════════════════════════════════════════════════════");
        System.out.println();
    }

    private void displayRoomStatus() {
        System.out.println("📍 Aktueller Raum: " + currentRoom.getName());
        System.out.println("❤️  Deine HP: " + player.getHp() + "/" + player.getMaxHp());

        List<Enemy> aliveEnemies = currentRoom.getAliveEnemies();
        if (!aliveEnemies.isEmpty()) {
            System.out.println("\n👹 Lebende Gegner:");
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
        System.out.println("═══════════════════════════\n");
    }

    private void displayGameStats() {
        System.out.println("\n📈 ═══ SPIELSTATISTIKEN ═══");
        System.out.println("🏛️  Räume abgeschlossen: " + roomsCleared);
        System.out.println("💀 Gegner besiegt: " + totalEnemiesKilled);
        System.out.println("🎯 Fortschritt: " + roomsCleared + "/5 Räume");
        System.out.println("═══════════════════════════\n");
    }

    private void displayRoomCleared() {
        System.out.println("\n🎉 ══════════════════════════════════════════════════════════════");
        System.out.println("   ✨ RAUM ERFOLGREICH ABGESCHLOSSEN! ✨");
        System.out.println("══════════════════════════════════════════════════════════════");

        // Spieler heilen
        int healAmount = 20;
        player.heal(healAmount);
        System.out.println("💚 Du erholst dich und erhältst " + healAmount + " HP zurück!");
        System.out.println("❤️  Aktuelle HP: " + player.getHp() + "/" + player.getMaxHp());
        System.out.println();
    }

    private void displayVictory() {
        System.out.println("\n🏆 ══════════════════════════════════════════════════════════════");
        System.out.println("   🎊 HERZLICHEN GLÜCKWUNSCH! 🎊");
        System.out.println("   Du hast das gesamte Abenteuer erfolgreich abgeschlossen!");
        System.out.println("   Alle " + roomsCleared + " Räume wurden erobert!");
        System.out.println("   " + totalEnemiesKilled + " Gegner wurden besiegt!");
        System.out.println("══════════════════════════════════════════════════════════════");
    }

    private void displayGameOver() {
        System.out.println("\n💀 ══════════════════════════════════════════════════════════════");
        System.out.println("   ⚰️  GAME OVER ⚰️");
        System.out.println("   Dein Abenteuer endet hier...");
        System.out.println("   Räume abgeschlossen: " + roomsCleared);
        System.out.println("   Gegner besiegt: " + totalEnemiesKilled);
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
        int numberOfEnemies = random.nextInt(3) + 1; // 1-3 Gegner

        this.setCurrentRoom(new RoomBuilder().withTemplate(template, numberOfEnemies).build());
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
}