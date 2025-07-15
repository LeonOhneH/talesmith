import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
            /*
             * Enemy-Templates anlegen
             */
            List<EnemyTemplate> possibleEnemysRoom0 = new ArrayList<>();
            possibleEnemysRoom0.add(new EnemyTemplate("Klonkrieger", 20, 2, 80));
            possibleEnemysRoom0.add(new EnemyTemplate("DarthVader", 90, 10, 5));
            possibleEnemysRoom0.add(new EnemyTemplate("Obi-wan Keniobi", 150, 30, 20));
            possibleEnemysRoom0.add(new EnemyTemplate("Imperator Palpetin", 50, 10, 4));
            List<EnemyTemplate> possibleEnemysRoom1 = new ArrayList<>();
            possibleEnemysRoom1.add(new EnemyTemplate("DarthMaul", 20, 2, 10));
            possibleEnemysRoom1.add(new EnemyTemplate("Obi-wan Keniobi", 150, 30, 20));
            possibleEnemysRoom1.add(new EnemyTemplate("Troide", 10, 5, 2));
            List<EnemyTemplate> possibleEnemysRoom2 = new ArrayList<>();
            possibleEnemysRoom2.add(new EnemyTemplate("DarthVader", 90, 10, 5));
            possibleEnemysRoom2.add(new EnemyTemplate("Klonkrieger", 20, 2, 80));
            List<EnemyTemplate> possibleEnemysRoom3 = new ArrayList<>();
            possibleEnemysRoom3.add(new EnemyTemplate("Obi-wan Keniobi", 150, 30, 20));
            possibleEnemysRoom3.add(new EnemyTemplate("DarthMaul", 20, 2, 10));
            possibleEnemysRoom3.add(new EnemyTemplate("Bobafett", 200, 5, 1));
            List<EnemyTemplate> possibleEnemysRoom4 = new ArrayList<>();
            possibleEnemysRoom4.add(new EnemyTemplate("Imperator Palpetin", 50, 10, 4));
            possibleEnemysRoom4.add(new EnemyTemplate("Yoda", 30, 30, 20));
            possibleEnemysRoom4.add(new EnemyTemplate("Klonkrieger", 20, 2, 80));
    
            /*
             * Raum-Templates anlegen und mit möglichen Gegnern verknüpfen
             * (hier einfach alle Gegner als mögliche Gegner hinterlegt)
             */
            List<RoomTemplate> roomTemplates = new ArrayList<>();
            roomTemplates.add(new RoomTemplate("Todesstern", possibleEnemysRoom0, 4, 10));
            roomTemplates.add(new RoomTemplate("Palast auf Naboo", possibleEnemysRoom1, 8, 15));
            roomTemplates.add(new RoomTemplate("Endor", possibleEnemysRoom2, 6, 12));
            roomTemplates.add(new RoomTemplate("Tatoine", possibleEnemysRoom3, 3, 6));
            roomTemplates.add(new RoomTemplate("Corussant", possibleEnemysRoom4, 6, 9));
    
            /*
             * Spieler und Spielinstanz erzeugen und Spiel starten
             */
            Player player = new Player("Luke Skywalker", 100, 50, 40);
            Game game = new Game("StarWars", roomTemplates, possibleEnemysRoom0, player);
            game.start();
        }
}