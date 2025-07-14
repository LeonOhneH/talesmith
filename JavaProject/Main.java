import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        /*
         * 1️⃣  Enemy-Templates anlegen
         */
        List<EnemyTemplate> enemyTemplates = new ArrayList<>();
        enemyTemplates.add(new EnemyTemplate("Goblin", 10, 5, 3));
        enemyTemplates.add(new EnemyTemplate("Skelett", 15, 6, 2));
        enemyTemplates.add(new EnemyTemplate("Ork", 25, 8, 2));
        enemyTemplates.add(new EnemyTemplate("Troll", 40, 10, 1));
        enemyTemplates.add(new EnemyTemplate("Drache", 100, 20, 5));

        /*
         * 2️⃣  Raum-Templates anlegen und mit möglichen Gegnern verknüpfen
         *     (hier einfach alle Gegner als mögliche Gegner hinterlegt)
         */
        List<RoomTemplate> roomTemplates = new ArrayList<>();
        roomTemplates.add(new RoomTemplate("Waldlichtung", enemyTemplates));
        roomTemplates.add(new RoomTemplate("Verlassener Kerker", enemyTemplates));
        roomTemplates.add(new RoomTemplate("Alte Burghalle", enemyTemplates));
        roomTemplates.add(new RoomTemplate("Dunkle Höhle", enemyTemplates));

        /*
         * 3️⃣  Spielinstanz erzeugen und starten
         */
        Game game = new Game("AdventureQuest", roomTemplates, enemyTemplates);
        game.start();
    }
}
