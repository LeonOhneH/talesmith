import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
            /*
             * Enemy-Templates anlegen
             */
            List<EnemyTemplate> possibleEnemysRoom0 = new ArrayList<>();
            possibleEnemysRoom0.add(new EnemyTemplate("Vfl 1848 Bochum", 1111, 111, 12));
            possibleEnemysRoom0.add(new EnemyTemplate("Fc Bayern München", 111111, 111, 111111));
            List<EnemyTemplate> possibleEnemysRoom1 = new ArrayList<>();
            possibleEnemysRoom1.add(new EnemyTemplate("Vfl 1848 Bochum", 1111, 111, 12));
            possibleEnemysRoom1.add(new EnemyTemplate("1860 München", 100, 1, 2));
            possibleEnemysRoom1.add(new EnemyTemplate("Fc Bayern München", 111111, 111, 111111));
    
            /*
             * Raum-Templates anlegen und mit möglichen Gegnern verknüpfen
             * (hier einfach alle Gegner als mögliche Gegner hinterlegt)
             */
            List<RoomTemplate> roomTemplates = new ArrayList<>();
            roomTemplates.add(new RoomTemplate("Bochum", possibleEnemysRoom0));
            roomTemplates.add(new RoomTemplate("Bayern", possibleEnemysRoom1));
    
            /*
             * Spieler und Spielinstanz erzeugen und Spiel starten
             */
            Player player = new Player("AaronDerEndbossinDerFussballmannschaft", 1111111111, 11111, 111111111);
            Game game = new Game("Die Bundesliga", roomTemplates, possibleEnemysRoom0, player);
            game.start();
        }
}
