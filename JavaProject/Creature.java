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
        System.out.println("   💪 Angriffskraft: " + getAp());

        boolean criticalHit = Math.random() < 0.1;
        int damage = criticalHit ? (int) (getAp() * 1.5) : getAp();

        target.setHp(target.getHp() - damage);

        if (criticalHit) {
            System.out.println("💥 KRITISCHER TREFFER! Schaden: " + damage);
        } else {
            System.out.println("🗡️ Schaden verursacht: " + damage);
        }

        // Health Bar nach Angriff anzeigen
        displaySimpleHealthBar(target);

        if (target.isDead()) {
            System.out.println("💀 " + target.getName() + " wurde besiegt!");
        }
        System.out.println();
    }

    private void displaySimpleHealthBar(Creature creature) {
        int maxHp = creature.getMaxHp();
        int currentHp = creature.getHp();

        double healthPercent = (double) currentHp / maxHp;

        // Bestimme Icon basierend auf HP-Prozentsatz
        String healthIcon;
        if (healthPercent > 0.75) {
            healthIcon = "💚";
        } else if (healthPercent > 0.5) {
            healthIcon = "💛";
        } else if (healthPercent > 0.25) {
            healthIcon = "🧡";
        } else if (healthPercent > 0) {
            healthIcon = "❤️";
        } else {
            healthIcon = "💀";
        }

        // Kompakte Health Bar (10 Zeichen)
        int barWidth = 10;
        int filledBars = (int) (healthPercent * barWidth);
        int emptyBars = barWidth - filledBars;

        StringBuilder healthBar = new StringBuilder();
        healthBar.append(healthIcon).append(" ");

        for (int i = 0; i < filledBars; i++) {
            healthBar.append("█");
        }

        for (int i = 0; i < emptyBars; i++) {
            healthBar.append("░");
        }

        healthBar.append(" ").append(currentHp).append("/").append(maxHp);

        System.out.println("❤️ " + creature.getName() + ": " + healthBar.toString());
    }

    @Override
    public String toString() {
        return name + " (HP: " + hp + "/" + maxHp + ", AP: " + ap + ", Agility: " + agility + ")";
    }
}