class Player extends Creature {
    private int experience;
    private int level;

    public Player(String name, int hp, int ap, int agility) {
        super(name, hp, ap, agility);
        this.experience = 0;
        this.level = 1;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void gainExperience(int exp) {
        this.experience += exp;
        checkLevelUp();
    }

    private void checkLevelUp() {
        int expNeeded = level * 10;
        if (experience >= expNeeded) {
            level++;
            experience -= expNeeded;

            // Statusverbesserungen beim Level-Up
            int hpIncrease = 20;
            int apIncrease = 2;
            int agilityIncrease = 1;

            setMaxHp(getMaxHp() + hpIncrease);
            setHp(getMaxHp());
            setAp(getAp() + apIncrease);
            setAgility(getAgility() + agilityIncrease);

            System.out.println("🎊 LEVEL UP! Du bist jetzt Level " + level + "!");
            System.out.println("📈 HP +" + hpIncrease + ", AP +" + apIncrease + ", Agility +" + agilityIncrease);
        }
    }

    @Override
    public String toString() {
        return super.toString() + " [Level: " + level + ", EXP: " + experience + "]";
    }
}