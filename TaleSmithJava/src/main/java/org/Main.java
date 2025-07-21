package main.java.org;

import main.java.org.templates.*;
import main.java.org.ui.ConsoleRenderer;
import main.java.org.ui.GameRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {
        public static void main(String[] args) {
                /*
                 * Enemy-Templates anlegen
                 */
                List<EnemyTemplate> possibleEnemysRoom0 = new ArrayList<>();
                HashMap<ItemTemplate, Float> possibleWeaponsEnemy0Room0 = new HashMap<ItemTemplate, Float>();
                possibleWeaponsEnemy0Room0.put( new WeaponTemplate( "Expo", "Framework to build mobile Apps", 40, WeaponTypeE.FRAMEWORK), 0.2f);
                possibleEnemysRoom0.add(new EnemyTemplate("Herr Althaus", 100, 5, 1, possibleWeaponsEnemy0Room0));
                HashMap<ItemTemplate, Float> possibleWeaponsEnemy1Room0 = new HashMap<ItemTemplate, Float>();
                possibleWeaponsEnemy1Room0.put( new WeaponTemplate( "Android manifest", "Configfile required by android apps", 20, WeaponTypeE.CONFIGFILE), 0.2f);
                possibleEnemysRoom0.add(new EnemyTemplate("Herr Schultes", 100, 20, 3, possibleWeaponsEnemy1Room0));
                List<EnemyTemplate> possibleEnemysRoom1 = new ArrayList<>();
                HashMap<ItemTemplate, Float> possibleWeaponsEnemy0Room1 = new HashMap<ItemTemplate, Float>();
                possibleWeaponsEnemy0Room1.put( new WeaponTemplate( "EMF", "Eclipse Modeling Framework", 30, WeaponTypeE.FRAMEWORK), 0.2f);
                possibleEnemysRoom1.add(new EnemyTemplate("Herr Hoffmann", 150, 50, 10, possibleWeaponsEnemy0Room1));
                HashMap<ItemTemplate, Float> possibleWeaponsEnemy1Room1 = new HashMap<ItemTemplate, Float>();
                possibleWeaponsEnemy1Room1.put( new WeaponTemplate( "EMF", "Eclipse Modeling Framework", 30, WeaponTypeE.FRAMEWORK), 0.2f);
                possibleEnemysRoom1.add(new EnemyTemplate("Herr Wagner", 110, 20, 2, possibleWeaponsEnemy1Room1));

                /*
                 * Raum-Templates anlegen und mit möglichen Gegnern verknüpfen
                 * (hier einfach alle Gegner als mögliche Gegner hinterlegt)
                 */
                List<RoomTemplate> roomTemplates = new ArrayList<>();
                HashMap<ItemTemplate, Float> possibleWeaponsRoom0 = new HashMap<ItemTemplate, Float>();
                possibleWeaponsRoom0.put( new WeaponTemplate( "Android manifest", "Configfile required by android apps", 20, WeaponTypeE.CONFIGFILE), 0.2f);
                possibleWeaponsRoom0.put( new WeaponTemplate( "Expo", "Framework to build mobile Apps", 40, WeaponTypeE.FRAMEWORK), 0.2f);
                roomTemplates.add(new RoomTemplate("Mobile Technologien", possibleEnemysRoom0, 1, 4, possibleWeaponsRoom0));
                HashMap<ItemTemplate, Float> possibleWeaponsRoom1 = new HashMap<ItemTemplate, Float>();
                possibleWeaponsRoom1.put( new WeaponTemplate( "EMF", "Eclipse Modeling Framework", 30, WeaponTypeE.FRAMEWORK), 0.2f);
                roomTemplates.add(new RoomTemplate("Modellgetriebene Softwareentwicklung", possibleEnemysRoom1, 1, 4, possibleWeaponsRoom1));

                /*
                 * Spieler und Spielinstanz erzeugen und Spiel starten
                 */
                Player player = new Player("Leon Schäfer", 100, 10, 3);
                GameRenderer renderer = new ConsoleRenderer();
                Game game = new Game("Softwaretechnologie BSc.", roomTemplates, player, renderer);
                game.start();
        }
}