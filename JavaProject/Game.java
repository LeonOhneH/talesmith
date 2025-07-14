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
        setPlayer(new Player("Leon", 100, 10, 10));

        gameLoop();
    }

    public int gameLoop() {
        // Placeholder for the main game loop
        System.out.println("Running game loop...");
        while (true) {
            int retInt = roomLoop();
            if (retInt == 0) {
                System.out.println("Advancing to next room ...");
            } else {
                return retInt;
            }

        }
    }

    public int roomLoop() {
        generateNewRoom();

        while (!currentRoom.isCleared()) {

            if (player.isDead()) {
                System.out.println("Player died, game ends");
                return 1;
            }

            System.out.println(currentRoom);

            System.out.println();
            System.out.println("What do you want to do? [f]ight");

            Scanner s = new Scanner(System.in);
            String input = s.next();


            switch (input) {
                case "f":
                case "F":
                    System.out.println();
                    System.out.println("What enemy do you want to fight? [1]st enemy, [2]nd enemy");

                    int inputEnemy = s.nextInt() - 1;

                    currentRoom.fight(player, currentRoom.getEnemies().get(inputEnemy));
                    break;
            }
        }

        System.out.println("The Room has been cleared!");
        return 0;
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