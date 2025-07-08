abstract class Creature {
    protected String name;
    protected int hp;
    protected int ap;
    protected int agility;

    public Creature(String name, int hp, int ap, int agility) {
        this.name = name;
        this.hp = hp;
        this.ap = ap;
        this.agility = agility;
    }
}