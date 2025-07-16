package main.java.org.service;

import main.java.org.Creature;
import main.java.org.Enemy;
import main.java.org.Player;
import main.java.org.Weapon;
import main.java.org.Item;

import java.util.ArrayList;
import java.util.List;

public class CombatService {

    public CombatResult executeCombat(Player player, Enemy enemy) {
        boolean playerFirst = player.getAgility() >= enemy.getAgility();
        int rounds = 0;
        List<AttackResult> attackHistory = new ArrayList<>();

        while (enemy.isAlive() && player.isAlive() && rounds < 20) {
            rounds++;

            if (playerFirst) {
                AttackResult playerAttack = executeAttack(player, enemy);
                attackHistory.add(playerAttack);

                if (enemy.isAlive()) {
                    AttackResult enemyAttack = executeAttack(enemy, player);
                    attackHistory.add(enemyAttack);
                }
            } else {
                AttackResult enemyAttack = executeAttack(enemy, player);
                attackHistory.add(enemyAttack);

                if (player.isAlive()) {
                    AttackResult playerAttack = executeAttack(player, enemy);
                    attackHistory.add(playerAttack);
                }
            }
        }

        CombatOutcome outcome;
        if (player.isDead()) {
            outcome = CombatOutcome.PLAYER_DEFEATED;
        } else if (enemy.isDead()) {
            outcome = CombatOutcome.ENEMY_DEFEATED;
        } else {
            outcome = CombatOutcome.TIME_LIMIT;
        }

        return new CombatResult(outcome, rounds, playerFirst, attackHistory);
    }

    public AttackResult executeAttack(Creature attacker, Creature target) {
        boolean criticalHit = Math.random() < 0.1;

        // Berechne Gesamtschaden (AP + Waffen-Damage)
        int totalAttackPower = calculateTotalAttackPower(attacker);
        int damage = criticalHit ? (int) (totalAttackPower * 1.5) : totalAttackPower;

        int oldHp = target.getHp();
        target.setHp(target.getHp() - damage);
        int newHp = target.getHp();

        return new AttackResult(
                attacker.getName(),
                target.getName(),
                damage,
                criticalHit,
                oldHp,
                newHp,
                target.isDead());
    }

    private int calculateTotalAttackPower(Creature attacker) {
        int baseAP = attacker.getAp();

        // Wenn es ein Player ist, checke nach Waffen im Inventory
        if (attacker instanceof Player) {
            Player player = (Player) attacker;
            int weaponDamage = getBestWeaponDamage(player);
            return baseAP + weaponDamage;
        }

        return baseAP;
    }

    private int getBestWeaponDamage(Player player) {
        int maxDamage = 0;

        for (Item item : player.getInventory()) {
            if (item instanceof Weapon) {
                Weapon weapon = (Weapon) item;
                if (weapon.getDamage() > maxDamage) {
                    maxDamage = weapon.getDamage();
                }
            }
        }

        return maxDamage;
    }

    public int calculateExperienceGain(Enemy enemy) {
        return enemy.getAp() + enemy.getMaxHp() / 10;
    }

    public boolean determineAttackOrder(Player player, Enemy enemy) {
        return player.getAgility() >= enemy.getAgility();
    }

    public static class CombatResult {
        private final CombatOutcome outcome;
        private final int rounds;
        private final boolean playerFirst;
        private final List<AttackResult> attackHistory;

        public CombatResult(CombatOutcome outcome, int rounds, boolean playerFirst, List<AttackResult> attackHistory) {
            this.outcome = outcome;
            this.rounds = rounds;
            this.playerFirst = playerFirst;
            this.attackHistory = attackHistory;
        }

        public CombatOutcome getOutcome() {
            return outcome;
        }

        public int getRounds() {
            return rounds;
        }

        public boolean isPlayerFirst() {
            return playerFirst;
        }

        public List<AttackResult> getAttackHistory() {
            return attackHistory;
        }
    }

    public static class AttackResult {
        private final String attackerName;
        private final String targetName;
        private final int damage;
        private final boolean criticalHit;
        private final int oldHp;
        private final int newHp;
        private final boolean targetDied;

        public AttackResult(String attackerName, String targetName, int damage,
                boolean criticalHit, int oldHp, int newHp, boolean targetDied) {
            this.attackerName = attackerName;
            this.targetName = targetName;
            this.damage = damage;
            this.criticalHit = criticalHit;
            this.oldHp = oldHp;
            this.newHp = newHp;
            this.targetDied = targetDied;
        }

        // Getter
        public String getAttackerName() {
            return attackerName;
        }

        public String getTargetName() {
            return targetName;
        }

        public int getDamage() {
            return damage;
        }

        public boolean isCriticalHit() {
            return criticalHit;
        }

        public int getOldHp() {
            return oldHp;
        }

        public int getNewHp() {
            return newHp;
        }

        public boolean isTargetDied() {
            return targetDied;
        }
    }

    public enum CombatOutcome {
        PLAYER_DEFEATED,
        ENEMY_DEFEATED,
        TIME_LIMIT
    }
}