package main.java.org;

import main.java.org.builder.RoomBuilder;
import main.java.org.service.CombatService;
import main.java.org.service.CombatService.*;
import main.java.org.service.DropService;
import main.java.org.templates.EnemyTemplate;
import main.java.org.templates.RoomTemplate;
import main.java.org.templates.WeaponTypeE;
import main.java.org.ui.GameRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Game {
    private String name;
    private List<RoomTemplate> roomTemplates;
    private List<EnemyTemplate> enemyTemplates;

    private Player player;
    private Room currentRoom;
    private int difficultyLevel;

    private int roomsCleared;
    private int totalEnemiesKilled;

    private Random random;
    private GameRenderer renderer;
    private CombatService combatService;
    private DropService dropService;

    public Game(GameRenderer renderer) {
        this.renderer = renderer;
        this.combatService = new CombatService();
        this.dropService = new DropService();
        this.random = new Random();
        this.roomsCleared = 0;
        this.totalEnemiesKilled = 0;
        this.difficultyLevel = 1;
    }

    public Game(String name, List<RoomTemplate> roomTemplates, Player player, GameRenderer renderer) {
        this(renderer);
        this.setName(name);
        this.setRoomTemplates(roomTemplates);
        this.setPlayer(player);
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

        renderer.drawBox(name, welcomeContent);
        renderer.getInput();
        renderer.clearScreen();
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
                renderer.sleep(1000);
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

        renderer.drawBox("LEVEL UP!", content);

        int bonusHeal = 30 + (difficultyLevel * 10);
        player.heal(bonusHeal);

        renderer.sleep(2000);
        renderer.clearScreen();
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

        renderer.drawBox("SPIEL BEENDET", content);

        String input = renderer.getInput().toLowerCase();
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

    private void handleFight() {
        List<Enemy> aliveEnemies = currentRoom.getAliveEnemies();
        if (aliveEnemies.isEmpty()) {
            String[] content = {
                    "",
                    "🎉 Alle Gegner wurden bereits besiegt!",
                    "",
                    "Drücke Enter um fortzufahren..."
            };
            renderer.drawBox("KAMPF", content);
            renderer.getInput();
            renderer.clearScreen();
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

        renderer.drawBox("GEGNER WÄHLEN", content.toArray(new String[0]));

        int choice = renderer.getValidInput(1, aliveEnemies.size());
        Enemy targetEnemy = aliveEnemies.get(choice - 1);

        renderer.clearScreen();

        // Kampf über CombatService ausführen
        executeCombatWithAnimation(player, targetEnemy);

        if (targetEnemy.isDead()) {
            totalEnemiesKilled++;

            // Level-Up Behandlung
            int oldLevel = player.getLevel();
            int expGain = combatService.calculateExperienceGain(targetEnemy);
            player.gainExperience(expGain);

            if (player.getLevel() > oldLevel) {
                displayPlayerLevelUp(oldLevel, player.getLevel());
            }

            // HINZUGEFÜGT: Drop-System
            handleEnemyDrops(targetEnemy);

            int healAmount = 5;
            player.heal(healAmount);

            currentRoom.checkCleared();
        }
    }

    private void handleEnemyDrops(Enemy enemy) {
        // 1. Drops aus dem Raum
        List<Item> roomDrops = currentRoom.getDrops();
        if (roomDrops != null && !roomDrops.isEmpty() && random.nextFloat() < 0.15f) { // 15% Chance
            Item droppedItem = roomDrops.get(random.nextInt(roomDrops.size()));
            player.addItem(droppedItem);
            displayItemFound(droppedItem);
            return; // Wenn ein Raum-Drop erfolgt, keinen Gegner-Drop mehr
        }

        // 2. Gegnerspezifische Drops vom Template mit dem DropService
        if (enemy.getTemplate() != null && enemy.getTemplate().getPossibleDrops() != null) {
            // Hier benutzen wir den dropService um Items zu generieren
            List<Item> enemyDrops = dropService.generateDrops(enemy.getTemplate().getPossibleDrops());

            if (!enemyDrops.isEmpty()) {
                // Nehme einen zufälligen Drop aus der Liste
                Item droppedItem = enemyDrops.get(random.nextInt(enemyDrops.size()));
                player.addItem(droppedItem);
                displayItemFound(droppedItem);
                return;
            }
        }

        // 3. Fallback: Generisches Item, wenn kein Template-Drop verfügbar
        if (random.nextFloat() < 0.3f) { // 30% Chance
            // Erzeuge einen zufälligen Gegenstand für den Gegner
            Weapon droppedWeapon = new Weapon(
                    "Waffe von " + enemy.getName(),
                    "Erbeutet von " + enemy.getName(),
                    5 + random.nextInt(10), // Zufälliger Schaden 5-15
                    WeaponTypeE.Schlag // Standard-Typ
            );

            player.addItem(droppedWeapon);
            displayItemFound(droppedWeapon);
        }
    }

    private void displayItemFound(Item item) {
        String itemType = item instanceof Weapon ? "⚔️ Waffe" : "🧪 Gegenstand";

        String[] content = {
                "",
                "🎁 GEGENSTAND GEFUNDEN! 🎁",
                "",
                itemType + ": " + item.getName(),
                "",
                "📝 " + item.getDescription(),
                "",
                "✅ Zum Inventar hinzugefügt!",
                "",
                "Drücke Enter um fortzufahren..."
        };

        renderer.drawBox("LOOT!", content);
        renderer.getInput();
        renderer.clearScreen();
    }

    private void displayPlayerStats() {
        // Berechne Gesamtangriffskraft mit bester Waffe
        int weaponDamage = getBestWeaponDamage();
        int totalAP = player.getAp() + weaponDamage;

        String[] content = {
                "",
                "👤 " + player.getName(),
                "",
                getHealthBarString(player),
                "",
                "⚔️ Angriffskraft: " + player.getAp() + (weaponDamage > 0 ? " + " + weaponDamage + " (Waffe)" : ""),
                "💪 Gesamt-AP: " + totalAP,
                "⚡ Geschwindigkeit: " + player.getAgility(),
                "🎯 Level: " + player.getLevel(),
                "✨ Erfahrung: " + player.getExperience(),
                "🎒 Inventar: " + player.getInventory().size() + " Gegenstände",
                "",
                "Drücke Enter um zurückzukehren..."
        };

        renderer.drawBox("CHARAKTERWERTE", content);
        renderer.getInput();
        renderer.clearScreen();
    }

    private int getBestWeaponDamage() {
        int maxDamage = 0;
        for (Item item : player.getInventory()) {
            if (item instanceof Weapon) {
                Weapon weapon = (Weapon) item;
                if (weapon.getDamage() > maxDamage) {
                    maxDamage = weapon.getDamage();
                }
            }
        }
        return maxDamage;
    }

    // NEUE METHODE: Inventar anzeigen
    private void displayInventory() {
        List<String> content = new ArrayList<>();
        content.add("");
        content.add("🎒 INVENTAR");
        content.add("");

        if (player.getInventory().isEmpty()) {
            content.add("📭 Inventar ist leer!");
        } else {
            for (int i = 0; i < player.getInventory().size(); i++) {
                Item item = player.getInventory().get(i);
                String itemIcon = item instanceof Weapon ? "⚔️" : "🧪";
                content.add((i + 1) + ". " + itemIcon + " " + item.getName());

                if (item instanceof Weapon) {
                    Weapon weapon = (Weapon) item;
                    content.add("   └ Schaden: +" + weapon.getDamage());
                }
            }
        }

        content.add("");
        content.add("Drücke Enter um zurückzukehren...");

        renderer.drawBox("INVENTAR", content.toArray(new String[0]));
        renderer.getInput();
        renderer.clearScreen();
    }

    public int roomLoop() {
        generateNewRoom();
        displayRoomEntry();

        while (!currentRoom.isCleared()) {
            if (player.isDead()) {
                return 1;
            }

            displayRoomStatus();
            int choice = renderer.getValidInput(1, 5); // GEÄNDERT: 5 Optionen statt 4

            switch (choice) {
                case 1:
                    handleFight();
                    break;
                case 2:
                    displayPlayerStats();
                    break;
                case 3:
                    displayInventory(); // HINZUGEFÜGT
                    break;
                case 4:
                    displayGameStats();
                    break;
                case 5:
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

        renderer.drawBox("NEUER RAUM", content);
        renderer.getInput();
        renderer.clearScreen();
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
        content.add("1. 🗡️ Kämpfen  2. 📊 Stats  3. 🎒 Inventar  4. 📈 Info  5. 🚪 Beenden"); // GEÄNDERT
        content.add("");
        content.add("Deine Wahl: ");

        renderer.drawBox("AKTUELLER RAUM", content.toArray(new String[0]));
    }

    private void executeCombatWithAnimation(Player player, Enemy enemy) {
        // Kampf-Header anzeigen
        renderer.clearScreen();

        String[] headerContent = {
                "",
                "⚔️ KAMPF BEGINNT! ⚔️",
                "",
                player.getName() + " VS " + enemy.getName(),
                ""
        };
        renderer.drawBox("KAMPF", headerContent);
        renderer.sleep(750);

        // Angriffsreihenfolge bestimmen und anzeigen
        boolean playerFirst = combatService.determineAttackOrder(player, enemy);
        showCombatStats(player, enemy, playerFirst);

        // Kampf ausführen
        CombatResult result = combatService.executeCombat(player, enemy);

        // Kampfverlauf anzeigen
        showCombatRounds(result.getAttackHistory());

        // Kampfende anzeigen
        showCombatResult(result, player, enemy);
    }

    private void showCombatRounds(List<CombatService.AttackResult> attackHistory) {
        renderer.clearScreen();

        // Überschrift
        renderer.animatedPrint("⚔️ KAMPFVERLAUF ⚔️\n\n", 20);
        renderer.sleep(500);

        // Zeige maximal 6 Angriffe an (um nicht zu lang zu werden)
        int maxAttacks = Math.min(attackHistory.size(), 6);

        for (int i = 0; i < maxAttacks; i++) {
            CombatService.AttackResult attack = attackHistory.get(i);

            // Formatiere Angriffsnachricht
            StringBuilder message = new StringBuilder();

            // Kritischer Treffer?
            String criticalText = attack.isCriticalHit() ? "⚡ KRITISCH! ⚡ " : "";

            message.append(criticalText)
                    .append(attack.getAttackerName())
                    .append(" greift ")
                    .append(attack.getTargetName())
                    .append(" an und verursacht ")
                    .append(attack.getDamage())
                    .append(" Schaden!");

            // Zeige HP-Veränderung
            message.append(" (")
                    .append(attack.getOldHp())
                    .append(" → ")
                    .append(attack.getNewHp())
                    .append(" HP)");

            // Ist das Ziel gestorben?
            if (attack.isTargetDied()) {
                message.append("\n💀 ")
                        .append(attack.getTargetName())
                        .append(" wurde besiegt!");
            }

            // Animiere den Text
            renderer.animatedPrint(message.toString() + "\n\n", 15);
            renderer.sleep(500);
        }

        // Falls mehr als 6 Angriffe, zeige zusammenfassenden Text
        if (attackHistory.size() > 6) {
            renderer.animatedPrint("...\n(Der Kampf geht noch " + (attackHistory.size() - 6) +
                    " weitere Züge)\n\n", 15);
        }

        renderer.animatedPrint("Drücke Enter um fortzufahren...", 10);
        renderer.getInput();
    }

    private void showCombatStats(Player player, Enemy enemy, boolean playerFirst) {
        renderer.clearScreen();
        renderer.animatedPrint("👤 " + player.getName() + ":\n", 25);
        renderer.sleep(150);
        renderer.animatedPrint("   ❤️ HP: " + player.getHp() + "/" + player.getMaxHp() + "\n", 15);
        renderer.sleep(150);
        renderer.animatedPrint("   ⚔️ AP: " + player.getAp() + "\n", 15);
        renderer.sleep(150);
        renderer.animatedPrint("   ⚡ Speed: " + player.getAgility() + "\n", 15);

        renderer.sleep(400);

        renderer.animatedPrint("\n👹 " + enemy.getName() + ":\n", 25);
        renderer.sleep(150);
        renderer.animatedPrint("   ❤️ HP: " + enemy.getHp() + "/" + enemy.getMaxHp() + "\n", 15);
        renderer.sleep(150);
        renderer.animatedPrint("   ⚔️ AP: " + enemy.getAp() + "\n", 15);
        renderer.sleep(150);
        renderer.animatedPrint("   ⚡ Speed: " + enemy.getAgility() + "\n", 15);

        renderer.sleep(750);
        renderer.clearScreen();

        // KORRIGIERT: playerFirst Parameter verwenden statt result
        if (playerFirst) {
            renderer.animatedPrint("⚡ " + player.getName() + " ist schneller und greift zuerst an!\n", 20);
        } else {
            renderer.animatedPrint("⚡ " + enemy.getName() + " ist schneller und greift zuerst an!\n", 20);
        }

        renderer.sleep(1000);
    }

    private void showCombatResult(CombatResult result, Player player, Enemy enemy) {
        renderer.clearScreen();

        String[] content = new String[10];
        content[0] = "";
        content[1] = "🔥 KAMPF BEENDET! 🔥";
        content[2] = "";

        switch (result.getOutcome()) {
            case PLAYER_DEFEATED:
                content[3] = "💀 " + player.getName() + " wurde besiegt!";
                content[4] = "Das Abenteuer endet hier...";
                content[5] = "";
                content[6] = "";
                break;
            case ENEMY_DEFEATED:
                content[3] = "🎉 " + player.getName() + " hat gewonnen!";
                content[4] = "🏆 Sieg nach " + result.getRounds() + " Runden!";
                content[5] = "✨ Du erhältst Erfahrungspunkte!";
                content[6] = "💚 Du erholst dich etwas!";
                break;
            case TIME_LIMIT:
                content[3] = "⏰ Kampf nach " + result.getRounds() + " Runden beendet!";
                content[4] = "(Zeitlimit erreicht)";
                content[5] = "";
                content[6] = "";
                break;
        }

        content[7] = "";
        content[8] = "Drücke Enter um fortzufahren...";
        content[9] = "";

        renderer.drawBox("KAMPFERGEBNIS", content);
        renderer.getInput();
        renderer.clearScreen();
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

        renderer.drawBox("SPIELSTATISTIKEN", content);
        renderer.getInput();
        renderer.clearScreen();
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

        renderer.drawBox("SIEG!", content);

        int healAmount = Math.max(10, 25 - difficultyLevel);
        player.heal(healAmount);

        renderer.sleep(2000);
        renderer.clearScreen();
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

        renderer.drawBox("SPIEL BEENDET", content);
    }

    private boolean confirmQuit() {
        String[] content = {
                "",
                "❓ Möchtest du wirklich das Spiel beenden?",
                "",
                "(j/n): "
        };

        renderer.drawBox("SPIEL BEENDEN?", content);

        String input = renderer.getInput().toLowerCase();
        return input.equals("j") || input.equals("ja");
    }

    public void generateNewRoom() {
        RoomTemplate template = roomTemplates.get(random.nextInt(roomTemplates.size()));
        this.setCurrentRoom(new RoomBuilder()
                .withTemplate(template)
                .withDifficultyLevel(difficultyLevel)
                .generateEnemies()
                .build());
    }

    // Getter und Setter
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

    private void displayPlayerLevelUp(int oldLevel, int newLevel) {
        String[] content = {
                "",
                "🎊 LEVEL UP! 🎊",
                "",
                "Level " + oldLevel + " → Level " + newLevel,
                "",
                "📈 Statusverbesserungen:",
                "❤️ HP +20 (vollständig geheilt)",
                "⚔️ AP +2",
                "⚡ Agility +1",
                "",
                "Drücke Enter um fortzufahren..."
        };

        renderer.drawBox("LEVEL UP!", content);
        renderer.getInput();
        renderer.clearScreen();
    }
}