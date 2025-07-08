class EnemyBuilder {
    private String name;
    private int hp;
    private int ap;
    private int agility;

    public EnemyBuilder(EnemyTemplate template) {
        this.name = template.getName();
        this.hp = template.getHp();
        this.ap = template.getAp();
        this.agility = template.getAgility();
    }

    public EnemyBuilder withTemplate(EnemyTemplate template) {
        this.name = template.getName();
        this.hp = template.getHp();
        this.ap = template.getAp();
        this.agility = template.getAgility();
        return this;
    }

    public EnemyBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public EnemyBuilder withHp(int hp) {
        this.hp = hp;
        return this;
    }

    public EnemyBuilder withAp(int ap) {
        this.ap = ap;
        return this;
    }

    public EnemyBuilder withAgility(int agility) {
        this.agility = agility;
        return this;
    }

    public Enemy build() {
        return new Enemy(name, hp, ap, agility);
    }
}
