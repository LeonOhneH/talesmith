package main.java.org;

import main.java.org.templates.*;
import main.java.org.ui.ConsoleRenderer;
import main.java.org.ui.GameRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {
        public static void main(String[] args) {

                HashMap<ItemTemplate, Float> possibleWeapons1 = new HashMap<ItemTemplate, Float>();
                possibleWeapons1.put(
                                new WeaponTemplate(
                                                "Lichtschwert",
                                                "Beste Schwert wo gibt",
                                                2000,
                                                WeaponTypeE.Schlag),
                                0.2f);

                HashMap<ItemTemplate, Float> possibleWeapons2 = new HashMap<ItemTemplate, Float>();
                possibleWeapons2.put(
                                new WeaponTemplate(
                                                "Blaster",
                                                "Stabiler Blaster",
                                                200,
                                                WeaponTypeE.Schieß),
                                0.2f);

                HashMap<ItemTemplate, Float> possibleWeapons3 = new HashMap<ItemTemplate, Float>();
                possibleWeapons3.put(
                                new WeaponTemplate(
                                                "Lichtschwert",
                                                "Beste Schwert wo gibt",
                                                2000,
                                                WeaponTypeE.Schlag),
                                0.1f);

                possibleWeapons3.put(
                                new WeaponTemplate(
                                                "Blaster",
                                                "Stabiler Blaster",
                                                200,
                                                WeaponTypeE.Schieß),
                                0.1f);

                /*
                 * Enemy-Templates anlegen
                 */
                List<EnemyTemplate> possibleEnemysRoom0 = new ArrayList<>();
                possibleEnemysRoom0.add(new EnemyTemplate("Klonkrieger", 20, 2, 80, possibleWeapons2));
                possibleEnemysRoom0.add(new EnemyTemplate("DarthVader", 90, 10, 5, possibleWeapons1));
                possibleEnemysRoom0.add(new EnemyTemplate("Obi-wan Keniobi", 150, 30, 20, possibleWeapons1));
                possibleEnemysRoom0.add(new EnemyTemplate("Imperator Palpetin", 50, 10, 4, possibleWeapons1));

                List<EnemyTemplate> possibleEnemysRoom1 = new ArrayList<>();
                possibleEnemysRoom1.add(new EnemyTemplate("DarthVader", 90, 10, 5, possibleWeapons1));
                possibleEnemysRoom1.add(new EnemyTemplate("Obi-wan Keniobi", 150, 30, 20, possibleWeapons1));
                possibleEnemysRoom1.add(new EnemyTemplate("Droide", 10, 5, 2, possibleWeapons2));

                List<EnemyTemplate> possibleEnemysRoom2 = new ArrayList<>();
                possibleEnemysRoom2.add(new EnemyTemplate("DarthVader", 90, 10, 5, possibleWeapons1));
                possibleEnemysRoom2.add(new EnemyTemplate("Klonkrieger", 20, 2, 80, possibleWeapons2));

                List<EnemyTemplate> possibleEnemysRoom3 = new ArrayList<>();
                possibleEnemysRoom3.add(new EnemyTemplate("Obi-wan Keniobi", 150, 30, 20, possibleWeapons1));
                possibleEnemysRoom3.add(new EnemyTemplate("DarthVader", 90, 10, 5, possibleWeapons1));
                possibleEnemysRoom3.add(new EnemyTemplate("Bobafett", 200, 5, 1, possibleWeapons2));

                List<EnemyTemplate> possibleEnemysRoom4 = new ArrayList<>();
                possibleEnemysRoom4.add(new EnemyTemplate("Imperator Palpetin", 50, 10, 4, possibleWeapons1));
                possibleEnemysRoom4.add(new EnemyTemplate("Yoda", 30, 30, 20, possibleWeapons1));
                possibleEnemysRoom4.add(new EnemyTemplate("Klonkrieger", 20, 2, 80, possibleWeapons2));

                /*
                 * Raum-Templates anlegen und mit möglichen Gegnern verknüpfen
                 * (hier einfach alle Gegner als mögliche Gegner hinterlegt)
                 */
                List<RoomTemplate> roomTemplates = new ArrayList<>();
                roomTemplates.add(new RoomTemplate("Todesstern", possibleEnemysRoom0, 4, 10, possibleWeapons3));
                roomTemplates.add(new RoomTemplate("Palast auf Naboo", possibleEnemysRoom1, 8, 15, possibleWeapons3));
                roomTemplates.add(new RoomTemplate("Endor", possibleEnemysRoom2, 6, 12, possibleWeapons3));
                roomTemplates.add(new RoomTemplate("Tatoine", possibleEnemysRoom3, 3, 6, possibleWeapons3));
                roomTemplates.add(new RoomTemplate("Corussant", possibleEnemysRoom4, 6, 9, possibleWeapons3));

                /*
                 * Spieler und Spielinstanz erzeugen und Spiel starten
                 */
                Player player = new Player("Luke Skywalker", 100, 50, 40);
                GameRenderer renderer = new ConsoleRenderer();
                Game game = new Game("StarWars", roomTemplates, player, renderer);
                game.start();
        }
}