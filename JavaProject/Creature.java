abstract class Creature {
    private String name;
    private int hp;
    private int maxHp;
    private int ap;
    private int agility;

    public Creature(String name, int hp, int ap, int agility) {
        this.setName(name);
        this.setHp(hp);
        this.setMaxHp(hp);
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
        this.hp = Math.max(0, hp);
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = Math.max(1, maxHp);
    }

    public int getAp() {
        return ap;
    }

    public void setAp(int ap) {
        this.ap = Math.max(0, ap);
    }

    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = Math.max(0, agility);
    }

    public boolean isDead() {
        return hp <= 0;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public void heal(int amount) {
        if (amount > 0) {
            this.hp = Math.min(maxHp, hp + amount);
        }
    }

    public void attack(Creature target) {
        if (target == null || this.isDead())
            return;

        System.out.println("⚔️ " + getName() + " greift " + target.getName() + " an!");

        boolean criticalHit = Math.random() < 0.1;
        int damage = criticalHit ? (int) (getAp() * 1.5) : getAp();

        target.setHp(target.getHp() - damage);

        if (criticalHit) {
            System.out.println("💥 KRITISCHER TREFFER! Schaden: " + damage);
        } else {
            System.out.println("🗡️ Schaden verursacht: " + damage);
        }

        System.out.println("❤️ " + target.getName() + " HP: " + target.getHp() + "/" + target.getMaxHp());

        if (target.isDead()) {
            System.out.println("💀 " + target.getName() + " wurde besiegt!");
        }
        System.out.println();
    }

    @Override
    public String toString() {
        return name + " (HP: " + hp + "/" + maxHp + ", AP: " + ap + ", Agility: " + agility + ")";
    }
}