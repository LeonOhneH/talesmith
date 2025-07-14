abstract class Creature {
    private String name;
    private int hp;
    private int ap;
    private int agility;

    public Creature(String name, int hp, int ap, int agility) {
        this.setName(name);
        this.setHp(hp);
        this.setAp(ap);
        this.setAgility(agility);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAp() {
        return ap;
    }

    public void setAp(int ap) {
        this.ap = ap;
    }

    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    @Override
    public String toString() {
        return "Creature{" +
                "name='" + name + '\'' +
                ", hp=" + hp +
                ", ap=" + ap +
                ", agility=" + agility +
                '}';
    }

}