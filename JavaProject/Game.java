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

    public Game() {

    }

    public Game(String name, List<RoomTemplate> roomTemplates, List<EnemyTemplate> enemyTemplates) {
        this.setName(name);
        this.setRoomTemplates(roomTemplates);
        this.setEnemyTemplates(enemyTemplates);
    }

    public void start() {
        System.out.println("Game started!");

        gameLoop();
    }

    public void gameLoop() {
        // Placeholder for the main game loop
        System.out.println("Running game loop...");
        generateNewRoom();

        while (!currentRoom.isCleared()) {
            System.out.println(currentRoom);

            Scanner s = new Scanner(System.in);
            String input = s.next();
            System.out.println("input " + input);
        }
    }

    public void generateNewRoom() {
        // Placeholder logic to generate room
        RoomTemplate template = getRoomTemplates().get(0);

        this.setCurrentRoom(new RoomBuilder().withTemplate(template, 2).build());
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