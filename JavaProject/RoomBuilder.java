class RoomBuilder {
    private RoomTemplate template;
    private List<Enemy> enemies;

    public RoomBuilder(RoomTemplate template, List<Enemy> enemies) {
        this.template = template;
        this.enemies = enemies;
    }

    public RoomBuilder withTemplate(RoomTemplate template) {
        this.template = template;
        return this;
    }

    public RoomBuilder addEnemy(Enemy enemy) {
        this.enemies.add(enemy);
        return this;
    }

    public Room build() {
        return new Room(template.getName(), enemies);
    }
}