package com.narxoz.rpg.observer.impl;

import com.narxoz.rpg.observer.GameEvent;
import com.narxoz.rpg.observer.GameObserver;

import java.util.List;
import java.util.Random;

public class LootDropper implements GameObserver {

    private static final List<String> PHASE_LOOT = List.of(
            "Tarnished Amulet", "Cracked Rune Shard", "Vial of Boss Blood", "Shattered Gauntlet"
    );
    private static final List<String> DEFEAT_LOOT = List.of(
            "Crown of the Fallen King", "Soulbound Greatblade", "Heart of the Cursed Dungeon"
    );

    private final Random random = new Random();

    @Override
    public void onEvent(GameEvent event) {
        switch (event.getType()) {
            case BOSS_PHASE_CHANGED -> drop("Phase " + event.getValue() + " transition", PHASE_LOOT);
            case BOSS_DEFEATED -> drop("Boss defeated", DEFEAT_LOOT);
            default -> { }
        }
    }

    private void drop(String trigger, List<String> table) {
        String item = table.get(random.nextInt(table.size()));
        System.out.println("[LOOT] " + trigger + " — dropped: " + item + ".");
    }
}
