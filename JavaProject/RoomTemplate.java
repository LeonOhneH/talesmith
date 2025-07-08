class RoomTemplate {
    private String name;
    private List<EnemyTemplate> possibleEnemies;

    public RoomTemplate(String name, List<EnemyTemplate> possibleEnemies) {
        this.name = name;
        this.possibleEnemies = possibleEnemies;
    }

    public String getName() { return name; }
    public List<EnemyTemplate> getPossibleEnemies() { return possibleEnemies; }
}