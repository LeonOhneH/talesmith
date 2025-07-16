package main.java.org;

import java.util.ArrayList;

public class Player extends Creature {
    private int experience;
    private int level;
    private ArrayList<Item> inventory;

    public Player(String name, int hp, int ap, int agility) {
        super(name, hp, ap, agility);
        this.experience = 0;
        this.level = 1;
        this.inventory = new ArrayList<>();
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

    public ArrayList<Item> getInventory() {
        return inventory;
    }

    public void addItem(Item item) {
        this.inventory.add(item);
    }

    public void removeItem(Item item) {
        this.inventory.remove(item);
    }

    public void gainExperience(int exp) {
        this.experience += exp;
        checkLevelUp();
    }

    private void checkLevelUp() {
        // Erfahrungspunkte für nächstes Level: exponentiell steigend
        int expNeeded = level * 10 + (level * level * 5);

        if (experience >= expNeeded) {
            level++;
            experience -= expNeeded;

            // Statusverbesserungen beim Level-Up
            int hpIncrease = 20;
            int apIncrease = 2;
            int agilityIncrease = 1;

            setMaxHp(getMaxHp() + hpIncrease);
            setHp(getMaxHp()); // Vollständiges Heilen beim Level-Up
            setAp(getAp() + apIncrease);
            setAgility(getAgility() + agilityIncrease);
        }
    }

    // Neue Methode zur Berechnung der benötigten EXP für das nächste Level
    public int getExpNeededForNextLevel() {
        return level * 10 + (level * level * 5);
    }

    // Neue Methode für UI-Layer um Level-Up Informationen zu bekommen
    public LevelUpInfo getLastLevelUp() {
        return new LevelUpInfo(level, 20, 2, 1);
    }

    public static class LevelUpInfo {
        private final int newLevel;
        private final int hpIncrease;
        private final int apIncrease;
        private final int agilityIncrease;

        public LevelUpInfo(int newLevel, int hpIncrease, int apIncrease, int agilityIncrease) {
            this.newLevel = newLevel;
            this.hpIncrease = hpIncrease;
            this.apIncrease = apIncrease;
            this.agilityIncrease = agilityIncrease;
        }

        public int getNewLevel() {
            return newLevel;
        }

        public int getHpIncrease() {
            return hpIncrease;
        }

        public int getApIncrease() {
            return apIncrease;
        }

        public int getAgilityIncrease() {
            return agilityIncrease;
        }
    }

    @Override
    public String toString() {
        return super.toString() + " [Level: " + level + ", EXP: " + experience + ", Items: " + inventory.size() + "]";
    }
}