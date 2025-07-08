class Room {
    private String name;
    private List<Enemy> enemies;
    private boolean cleared;

    public Room(String name, List<Enemy> enemies) {
        this.name = name;
        this.enemies = enemies;
        this.cleared = false;
    }
}