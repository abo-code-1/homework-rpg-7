package com.narxoz.rpg.observer.impl;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.observer.GameEvent;
import com.narxoz.rpg.observer.GameEventType;
import com.narxoz.rpg.observer.GameObserver;

import java.util.List;

public class HeroStatusMonitor implements GameObserver {

    private final List<Hero> heroes;

    public HeroStatusMonitor(List<Hero> heroes) {
        this.heroes = heroes;
    }

    @Override
    public void onEvent(GameEvent event) {
        GameEventType type = event.getType();
        if (type != GameEventType.HERO_LOW_HP && type != GameEventType.HERO_DIED) {
            return;
        }
        StringBuilder line = new StringBuilder("[STATUS] Party: ");
        for (int i = 0; i < heroes.size(); i++) {
            Hero h = heroes.get(i);
            line.append(h.getName())
                .append(" ")
                .append(h.isAlive() ? h.getHp() + "/" + h.getMaxHp() : "DEAD");
            if (i < heroes.size() - 1) line.append(" | ");
        }
        System.out.println(line);
    }
}
