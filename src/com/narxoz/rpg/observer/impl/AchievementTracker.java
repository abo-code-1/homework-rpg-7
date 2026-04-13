package com.narxoz.rpg.observer.impl;

import com.narxoz.rpg.observer.GameEvent;
import com.narxoz.rpg.observer.GameObserver;

import java.util.HashSet;
import java.util.Set;

public class AchievementTracker implements GameObserver {

    private static final int RELENTLESS_THRESHOLD = 10;

    private final Set<String> unlocked = new HashSet<>();
    private int attacksLanded = 0;
    private int heroesLost = 0;

    @Override
    public void onEvent(GameEvent event) {
        switch (event.getType()) {
            case ATTACK_LANDED -> {
                attacksLanded++;
                if (attacksLanded == 1) {
                    unlock("First Blood", "first attack landed");
                }
                if (attacksLanded == RELENTLESS_THRESHOLD) {
                    unlock("Relentless", RELENTLESS_THRESHOLD + " attacks landed");
                }
            }
            case HERO_DIED -> heroesLost++;
            case BOSS_DEFEATED -> {
                unlock("Boss Slayer", "the boss has fallen");
                if (heroesLost == 0) {
                    unlock("No Man Left Behind", "victory with the full party alive");
                }
            }
            default -> { }
        }
    }

    private void unlock(String name, String reason) {
        if (unlocked.add(name)) {
            System.out.println("[ACHIEVEMENT] " + name + " unlocked — " + reason + ".");
        }
    }
}
