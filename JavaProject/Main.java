import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        /*
         * 🧌 Enemy-Templates anlegen - Vielfältige Gegner
         */
        List<EnemyTemplate> enemyTemplates = new ArrayList<>();

        // Schwache Gegner
        enemyTemplates.add(new EnemyTemplate("Goblin-Späher", 8, 4, 6));
        enemyTemplates.add(new EnemyTemplate("Riesige Ratte", 12, 3, 8));
        enemyTemplates.add(new EnemyTemplate("Zombi-Wanderer", 15, 5, 2));

        // Mittlere Gegner
        enemyTemplates.add(new EnemyTemplate("Goblin-Krieger", 18, 7, 5));
        enemyTemplates.add(new EnemyTemplate("Skelett-Soldat", 22, 8, 4));
        enemyTemplates.add(new EnemyTemplate("Ork-Räuber", 30, 10, 3));
        enemyTemplates.add(new EnemyTemplate("Dunkel-Elf", 25, 12, 9));

        // Starke Gegner
        enemyTemplates.add(new EnemyTemplate("Ork-Häuptling", 45, 15, 4));
        enemyTemplates.add(new EnemyTemplate("Troll-Wächter", 60, 18, 2));
        enemyTemplates.add(new EnemyTemplate("Schattenbestie", 40, 20, 12));
        enemyTemplates.add(new EnemyTemplate("Feuerdrache", 120, 25, 8));

        /*
         * 🏛️ Raum-Templates anlegen - Thematische Räume
         */
        List<RoomTemplate> roomTemplates = new ArrayList<>();

        // Einfachere Räume mit schwächeren Gegnern
        List<EnemyTemplate> forestEnemies = List.of(
                enemyTemplates.get(0), enemyTemplates.get(1), enemyTemplates.get(3));
        roomTemplates.add(new RoomTemplate("🌲 Verwunschener Wald", forestEnemies));

        List<EnemyTemplate> dungeonEnemies = List.of(
                enemyTemplates.get(2), enemyTemplates.get(4), enemyTemplates.get(5));
        roomTemplates.add(new RoomTemplate("🏰 Verlassener Kerker", dungeonEnemies));

        // Mittelschwere Räume
        List<EnemyTemplate> caveEnemies = List.of(
                enemyTemplates.get(3), enemyTemplates.get(5), enemyTemplates.get(7));
        roomTemplates.add(new RoomTemplate("🕳️ Dunkle Kristallhöhle", caveEnemies));

        List<EnemyTemplate> ruinsEnemies = List.of(
                enemyTemplates.get(4), enemyTemplates.get(6), enemyTemplates.get(8));
        roomTemplates.add(new RoomTemplate("🏛️ Verfallene Tempelruine", ruinsEnemies));

        // Schwierige Räume
        List<EnemyTemplate> strongEnemies = List.of(
                enemyTemplates.get(7), enemyTemplates.get(8), enemyTemplates.get(9), enemyTemplates.get(10));
        roomTemplates.add(new RoomTemplate("🔥 Drachenhort", strongEnemies));
        roomTemplates.add(new RoomTemplate("👑 Thronsaal des Bösen", strongEnemies));

        /*
         * 🎮 Spielinstanz erzeugen und starten
         */
        Game game = new Game("⚔️ MYSTIC QUEST ⚔️", roomTemplates, enemyTemplates);
        game.start();
    }
}
