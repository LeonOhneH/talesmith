class EnemyTemplate {
    private String name;
    private int hp;
    private int ap;
    private int agility;

    public EnemyTemplate(String name, int hp, int ap, int agility) {
        this.name = name;
        this.hp = hp;
        this.ap = ap;
        this.agility = agility;
    }

    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getAp() { return ap; }
    public int getAgility() { return agility; }
}