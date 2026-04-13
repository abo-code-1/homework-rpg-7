package com.narxoz.rpg.strategy;

public class BerserkStrategy implements CombatStrategy {

    @Override
    public int calculateDamage(int basePower) {
        return (int) Math.round(basePower * 1.4);
    }

    @Override
    public int calculateDefense(int baseDefense) {
        return (int) Math.round(baseDefense * 0.8);
    }

    @Override
    public String getName() {
        return "Berserk";
    }
}
