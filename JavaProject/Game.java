class Game {
    private String name;
    private List<RoomTemplate> roomTemplates;
    private List<EnemyTemplate> enemyTemplates;
    private Player player;
    private Room currentRoom;

    public Game(String name, List<RoomTemplate> roomTemplates, List<EnemyTemplate> enemyTemplates) {
        this.name = name;
        this.roomTemplates = roomTemplates;
        this.enemyTemplates = enemyTemplates;
    }

    public void start() {
        System.out.println("Game started!");
        generateNewRoom();
        gameLoop();
    }

    public void gameLoop() {
        // Placeholder for the main game loop
        System.out.println("Running game loop...");
    }

    public void generateNewRoom() {
        // Placeholder logic to generate room
        RoomTemplate template = roomTemplates.get(0);
        Enemy enemy = new EnemyBuilder(new EnemyTemplate("Goblin", 10, 5, 3)).build();
        RoomBuilder builder = new RoomBuilder(template, new ArrayList<>()).addEnemy(enemy);
        this.currentRoom = builder.build();
    }
}