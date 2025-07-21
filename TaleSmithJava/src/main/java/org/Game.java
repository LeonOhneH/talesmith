package main.java.org;

import main.java.org.builder.RoomBuilder;
import main.java.org.service.CombatService;
import main.java.org.service.CombatService.*;
import main.java.org.service.DropService;
import main.java.org.templates.*;
import main.java.org.ui.GameRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    private String name;
    private List<RoomTemplate> roomTemplates;
    private List<EnemyTemplate> enemyTemplates;
    private Player player;
    private Room currentRoom;
    private Random random;
    private int roomsCleared;
    private int totalEnemiesKilled;
    private int difficultyLevel;
    private GameRenderer renderer;
    private CombatService combatService;
    private DropService dropService;

    public Game(String name, List<RoomTemplate> roomTemplates, Player player, GameRenderer renderer) {
        this.name = name;
        this.roomTemplates = roomTemplates;
        this.player = player;
        this.renderer = renderer;
        this.random = new Random();
        this.roomsCleared = 0;
        this.totalEnemiesKilled = 0;
        this.difficultyLevel = 1;
        this.combatService = new CombatService();
        this.dropService = new DropService();
    }

    // Die start() Methode aktualisieren, um das Neustart-Feature zu unterstützen
    public void start() {
        boolean playAgain = true;

        while (playAgain) {
            // Spiel zurücksetzen, falls es nicht der erste Durchlauf ist
            resetGame();

            renderer.clearScreen();
            displayWelcome();

            int result = 0;
            while (result == 0 && !player.isDead()) {
                result = roomLoop();

                if (result == 0) { // Erfolgreich geschafft
                    roomsCleared++;
                    if (roomsCleared % 3 == 0) {
                        difficultyLevel++;
                    }
                    displayRoomCleared();
                }
            }

            // Spielende anzeigen und fragen, ob eine neue Runde gespielt werden soll
            playAgain = displayGameOver();
        }
    }

    // Diese Methode hinzufügen, um den Spielstatus zurückzusetzen
    private void resetGame() {
        roomsCleared = 0;
        totalEnemiesKilled = 0;
        difficultyLevel = 1;

        // Spieler zurücksetzen
        player.setHp(100);
        player.setMaxHp(100);
        player.setAp(50);
        player.setAgility(40);
        player.setLevel(1);
        player.setExperience(0);
        player.getInventory().clear();
    }

    private void displayWelcome() {
        String[] content = {
                "",
                "🌟 Willkommen bei " + name + "! 🌟",
                "",
                "Du bist " + player.getName() + ", ein tapferer Held!",
                "",
                "Deine Mission: Überlebe so viele Räume wie möglich!",
                "Mit jedem Raum steigt die Schwierigkeit.",
                "",
                "Viel Glück! ⚔️",
                "",
                "Drücke Enter um zu beginnen..."
        };

        renderer.drawBox("WILLKOMMEN", content);
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
            int choice = renderer.getValidInput(1, 5);

            switch (choice) {
                case 1:
                    handleFight();
                    break;
                case 2:
                    displayPlayerStats();
                    break;
                case 3:
                    displayInventory();
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
                "🏛️ Du betrittst " + currentRoom.getName() + "!",
                "",
                "👹 Feinde im Raum: " + currentRoom.getEnemies().size(),
                "",
                "🎯 Schwierigkeit: Level " + difficultyLevel,
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
        content.add("⚡ Initiative: " + player.getAgility() + "   ⚔️ AP: " + player.getAp());
        content.add("");

        // Gegner Status mit korrekter Ausrichtung
        List<Enemy> allEnemies = currentRoom.getEnemies();
        content.add("👹 Gegner (" + allEnemies.size() + "):");
        // Korrigierter Tabellenkopf mit festen Spaltenbreiten
        content.add("   # | Name                 | HP       | AP  | Initiative    ");
        content.add("   --+----------------------+----------+-----+--------------");

        // Alle Gegner anzeigen mit korrekter Ausrichtung
        for (int i = 0; i < allEnemies.size(); i++) {
            Enemy enemy = allEnemies.get(i);
            // Verwende ein konsistentes Format mit festen Spaltenbreiten
            String status = String.format("   %2d | %-20s | %3d/%-4d | %3d | %-12d",
                    (i + 1),
                    formatName(enemy.getName(), 20),
                    enemy.getHp(),
                    enemy.getMaxHp(),
                    enemy.getAp(),
                    enemy.getAgility());

            if (!enemy.isAlive()) {
                status += " 💀";
            }

            content.add(status);
        }

        content.add("");
        content.add("⚔️ Was möchtest du tun?");
        content.add("1. 🗡️ Kämpfen  2. 📊 Stats  3. 🎒 Inventar  4. 📈 Info  5. 🚪 Beenden");
        content.add("");
        content.add("Deine Wahl: ");

        renderer.drawBox("AKTUELLER RAUM", content.toArray(new String[0]));
    }

    // Hilfsmethode um Namen zu kürzen oder aufzufüllen
    private String formatName(String name, int length) {
        if (name.length() > length) {
            return name.substring(0, length - 3) + "...";
        }
        return name;
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

        // Gegner auswählen mit tabellarischer Darstellung
        List<String> content = new ArrayList<>();
        content.add("");
        content.add("⚔️ Welchen Gegner möchtest du angreifen?");
        content.add("");

        // Korrigierter Tabellenkopf mit festen Spaltenbreiten
        content.add("   # | Name                 | HP       | AP  | Initiative    ");
        content.add("   --+----------------------+----------+-----+--------------");

        // Gegner-Liste mit ausgerichteten Spalten
        for (int i = 0; i < aliveEnemies.size(); i++) {
            Enemy enemy = aliveEnemies.get(i);
            String formattedRow = String.format("   %2d | %-20s | %3d/%-4d | %3d | %-12d",
                    (i + 1),
                    formatName(enemy.getName(), 20),
                    enemy.getHp(),
                    enemy.getMaxHp(),
                    enemy.getAp(),
                    enemy.getAgility());

            content.add(formattedRow);
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

            // Item-Drops
            handleEnemyDrops(targetEnemy);

            int healAmount = 5;
            player.heal(healAmount);

            currentRoom.checkCleared();
        }
    }

    private void handleEnemyDrops(Enemy enemy) {
        // 1. Drops aus dem Raum
        List<Item> roomDrops = currentRoom.getDrops();
        if (roomDrops != null && !roomDrops.isEmpty() && random.nextFloat() < 0.05f) { // 5% Chance
            Item droppedItem = roomDrops.get(random.nextInt(roomDrops.size()));
            player.addItem(droppedItem);
            displayItemFound(droppedItem);
            return; // Wenn ein Raum-Drop erfolgt, keinen Gegner-Drop mehr
        }

        // 2. Gegnerspezifische Drops (10% Chance)
        if (enemy.getTemplate() != null && enemy.getTemplate().getPossibleDrops() != null &&
                random.nextFloat() < 0.3f) {
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

        // 3. Fallback: Generisches Item, wenn kein Template-Drop verfügbar (8% Chance)
        if (random.nextFloat() < 0.05f) {
            // Erzeuge einen zufälligen Gegenstand für den Gegner
            Weapon droppedWeapon = new Weapon(
                    "Waffe von " + enemy.getName(),
                    "Erbeutet von " + enemy.getName(),
                    5 + random.nextInt(10), // Zufälliger Schaden 5-15
                    WeaponTypeE.class.getEnumConstants()[0]
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

        int currentExp = player.getExperience();
        int expNeeded = player.getExpNeededForNextLevel();

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
                "✨ Erfahrung: " + currentExp + "/" + expNeeded,
                getExpBarString(currentExp, expNeeded),
                "🎒 Inventar: " + player.getInventory().size() + " Gegenstände",
                "",
                "Drücke Enter um zurückzukehren..."
        };

        renderer.drawBox("CHARAKTERWERTE", content);
        renderer.getInput();
        renderer.clearScreen();
    }

    private String getExpBarString(int currentExp, int expNeeded) {
        double expPercent = (double) currentExp / expNeeded;

        int barWidth = 15;
        int filledBars = (int) (expPercent * barWidth);
        StringBuilder expBar = new StringBuilder();

        expBar.append("✨ ");

        for (int i = 0; i < filledBars; i++) {
            expBar.append("█");
        }

        for (int i = filledBars; i < barWidth; i++) {
            expBar.append("░");
        }

        expBar.append(" ").append((int) (expPercent * 100)).append("%");

        return expBar.toString();
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

    private void displayPlayerLevelUp(int oldLevel, int newLevel) {
        renderer.clearScreen();
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

    // Die displayGameOver Methode anpassen, um eine Rückgabe für "Neue Runde" zu
    // haben
    private boolean displayGameOver() {
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
                "🎖️ " + rating,
                "",
                "Möchtest du noch eine Runde spielen? (j/n): "
        };

        renderer.drawBox("SPIEL BEENDET", content);

        // Eingabe abfragen und auswerten
        String input = renderer.getInput().toLowerCase();
        return input.equals("j") || input.equals("ja");
    }

    private void executeCombatWithAnimation(Player player, Enemy enemy) {
        // Nur EINE Box für den gesamten Kampf erstellen
        renderer.clearScreen();
        String[] initialContent = {
                "",
                "⚔️ KAMPF BEGINNT! ⚔️",
                "",
                player.getName() + " VS " + enemy.getName(),
                "",
                "Kampf wird vorbereitet...",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                ""
        };

        renderer.drawBox("KAMPF", initialContent);
        renderer.sleep(800);

        // Schritt 1: Kampfstatistiken in derselben Box zeigen
        boolean playerFirst = combatService.determineAttackOrder(player, enemy);
        String[] statsContent = {
                "",
                "⚔️ KAMPFSTATISTIK ⚔️",
                "",
                "👤 " + player.getName() + ":",
                "   ❤️ HP: " + player.getHp() + "/" + player.getMaxHp(),
                "   ⚔️ AP: " + player.getAp(),
                "   ⚡ Speed: " + player.getAgility(),
                "",
                "👹 " + enemy.getName() + ":",
                "   ❤️ HP: " + enemy.getHp() + "/" + enemy.getMaxHp(),
                "   ⚔️ AP: " + enemy.getAp(),
                "   ⚡ Speed: " + enemy.getAgility(),
                "",
                playerFirst ? "⚡ " + player.getName() + " ist schneller und greift zuerst an!"
                        : "⚡ " + enemy.getName() + " ist schneller und greift zuerst an!"
        };
        renderer.updateBoxContent(statsContent, 1);
        renderer.sleep(1500);

        // Kampf ausführen
        CombatResult result = combatService.executeCombat(player, enemy);

        // Schritt 2: Kampfverlauf in derselben Box animieren - OHNE Fortschrittsbalken
        List<CombatService.AttackResult> attackHistory = result.getAttackHistory();
        int maxAttacks = Math.min(attackHistory.size(), 10); // Erhöhe auf 10 für mehr sichtbare Angriffe
        List<String> combatLog = new ArrayList<>();

        // Titel für Kampfverlauf
        String[] progressHeader = {
                "",
                "⚔️ KAMPFVERLAUF ⚔️",
                "",
                "Der Kampf beginnt...",
                ""
        };
        renderer.updateBoxContent(progressHeader, 1);
        renderer.sleep(500);

        // Jetzt den Kampf Zug für Zug animieren
        for (int i = 0; i < maxAttacks; i++) {
            CombatService.AttackResult attack = attackHistory.get(i);
            combatLog.add(formatAttackMessage(attack));

            // Erstelle Array mit Header und bisherigen Kampfmeldungen
            String[] updatedContent = new String[3 + combatLog.size()];
            updatedContent[0] = "";
            updatedContent[1] = "⚔️ KAMPFVERLAUF ⚔️";
            updatedContent[2] = "";

            for (int j = 0; j < combatLog.size(); j++) {
                updatedContent[3 + j] = combatLog.get(j);
            }

            renderer.updateBoxContent(updatedContent, 1);
            renderer.sleep(600);
        }

        // Evtl. Zusammenfassung für weitere Angriffe
        if (attackHistory.size() > maxAttacks) {
            combatLog.add("..." + (attackHistory.size() - maxAttacks) + " weitere Angriffe...");
        }

        // Abschluss des Kampfverlaufs mit allen sichtbaren Angriffen
        String[] finalProgressContent = new String[3 + combatLog.size()];
        finalProgressContent[0] = "";
        finalProgressContent[1] = "⚔️ KAMPFVERLAUF ⚔️";
        finalProgressContent[2] = "";

        for (int j = 0; j < combatLog.size(); j++) {
            finalProgressContent[3 + j] = combatLog.get(j);
        }

        renderer.updateBoxContent(finalProgressContent, 1);
        renderer.sleep(1000);

        // Schritt 3: Kampfergebnis in derselben Box zeigen
        String outcomeMessage;
        String detailMessage;
        String expMessage = "";
        String healMessage = "";

        switch (result.getOutcome()) {
            case PLAYER_DEFEATED:
                outcomeMessage = "💀 " + player.getName() + " wurde besiegt!";
                detailMessage = "Das Abenteuer endet hier...";
                break;
            case ENEMY_DEFEATED:
                outcomeMessage = "🎉 " + player.getName() + " hat gewonnen!";
                detailMessage = "🏆 Sieg nach " + result.getRounds() + " Runden!";
                expMessage = "✨ Du erhältst Erfahrungspunkte!";
                healMessage = "💚 Du erholst dich etwas!";
                break;
            case TIME_LIMIT:
                outcomeMessage = "⏰ Kampf nach " + result.getRounds() + " Runden beendet!";
                detailMessage = "(Zeitlimit erreicht)";
                break;
            default:
                outcomeMessage = "Kampf beendet.";
                detailMessage = "";
        }

        String[] resultContent = {
                "",
                "🔥 KAMPF BEENDET! 🔥",
                "",
                outcomeMessage,
                detailMessage,
                expMessage,
                healMessage,
                "",
                "",
                "",
                "",
                "",
                "Drücke Enter um fortzufahren..."
        };

        renderer.updateBoxContent(resultContent, 1);
        renderer.getInput();
        renderer.clearScreen();
    }

    // Du kannst die alten Methoden löschen oder als private markieren,
    // da wir sie nicht mehr verwenden:
    // private void showCombatStatsInPlace(Player player, Enemy enemy) { ... }
    // private void showCombatRoundsInPlace(List<CombatService.AttackResult>
    // attackHistory) { ... }
    // private void showCombatResultInPlace(CombatResult result, Player player,
    // Enemy enemy) { ... }

    // Diese Methode behalten wir bei
    private String formatAttackMessage(CombatService.AttackResult attack) {
        StringBuilder message = new StringBuilder();

        // Kritischer Treffer?
        String criticalText = attack.isCriticalHit() ? "⚡ KRITISCH! ⚡ " : "";

        message.append(criticalText)
                .append(attack.getAttackerName())
                .append(" → ")
                .append(attack.getTargetName())
                .append(": ")
                .append(attack.getDamage())
                .append(" Schaden (")
                .append(attack.getOldHp())
                .append(" → ")
                .append(attack.getNewHp())
                .append(" HP)");

        if (attack.isTargetDied()) {
            message.append(" 💀");
        }

        return message.toString();
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

    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }
}