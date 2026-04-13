package com.narxoz.rpg.strategy;

public class DesperateStrategy implements CombatStrategy {

    @Override
    public int calculateDamage(int basePower) {
        return (int) Math.round(basePower * 1.8);
    }

    @Override
    public int calculateDefense(int baseDefense) {
        return (int) Math.round(baseDefense * 0.3);
    }

    @Override
    public String getName() {
        return "Desperate";
    }
}
