package com.narxoz.rpg.observer.impl;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.observer.GameEvent;
import com.narxoz.rpg.observer.GameEventType;
import com.narxoz.rpg.observer.GameObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PartySupport implements GameObserver {

    private static final int HEAL_AMOUNT = 25;

    private final List<Hero> heroes;
    private final Random random = new Random();

    public PartySupport(List<Hero> heroes) {
        this.heroes = heroes;
    }

    @Override
    public void onEvent(GameEvent event) {
        if (event.getType() != GameEventType.HERO_LOW_HP) {
            return;
        }
        List<Hero> living = new ArrayList<>();
        for (Hero h : heroes) {
            if (h.isAlive()) living.add(h);
        }
        if (living.isEmpty()) {
            return;
        }
        Hero target = living.get(random.nextInt(living.size()));
        target.heal(HEAL_AMOUNT);
        System.out.println("[SUPPORT] Cleric channels a prayer — " + target.getName()
                + " healed for " + HEAL_AMOUNT + " (now " + target.getHp() + "/" + target.getMaxHp() + ").");
    }
}
