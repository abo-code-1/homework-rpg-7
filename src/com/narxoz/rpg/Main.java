package com.narxoz.rpg;

import com.narxoz.rpg.combatant.DungeonBoss;
import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.engine.DungeonEngine;
import com.narxoz.rpg.engine.EncounterResult;
import com.narxoz.rpg.observer.GameEvent;
import com.narxoz.rpg.observer.GameEventType;
import com.narxoz.rpg.observer.GameObserver;
import com.narxoz.rpg.observer.GamePublisher;
import com.narxoz.rpg.observer.impl.AchievementTracker;
import com.narxoz.rpg.observer.impl.BattleLogger;
import com.narxoz.rpg.observer.impl.HeroStatusMonitor;
import com.narxoz.rpg.observer.impl.LootDropper;
import com.narxoz.rpg.observer.impl.PartySupport;
import com.narxoz.rpg.strategy.AggressiveStrategy;
import com.narxoz.rpg.strategy.BalancedStrategy;
import com.narxoz.rpg.strategy.DefensiveStrategy;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("   The Cursed Dungeon — Boss Encounter");
        System.out.println("============================================");

        GamePublisher publisher = new GamePublisher();

        Hero thoren = new Hero("Thoren", 140, 25, 10, new AggressiveStrategy());
        Hero mira   = new Hero("Mira",   110, 18, 14, new DefensiveStrategy());
        Hero kael   = new Hero("Kael",   100, 22,  8, new BalancedStrategy());
        List<Hero> heroes = List.of(thoren, mira, kael);

        DungeonBoss boss = new DungeonBoss("The Hollow Lich", 600, 25, 8, publisher);

        publisher.register(new BattleLogger());
        publisher.register(new HeroStatusMonitor(heroes));
        publisher.register(new AchievementTracker());
        publisher.register(new PartySupport(heroes));
        publisher.register(new LootDropper());

        publisher.register(new GameObserver() {
            @Override
            public void onEvent(GameEvent event) {
                if (event.getType() == GameEventType.BOSS_PHASE_CHANGED && event.getValue() == 2) {
                    mira.setStrategy(new BalancedStrategy());
                    System.out.println("[TACTICS] " + mira.getName()
                            + " drops her shield and shifts to a Balanced stance.");
                }
            }
        });

        System.out.println("Party:");
        for (Hero h : heroes) {
            System.out.println("  - " + h.getName() + " [" + h.getStrategy().getName()
                    + "] HP=" + h.getHp() + " ATK=" + h.getAttackPower() + " DEF=" + h.getDefense());
        }
        System.out.println("Boss: " + boss.getName() + " HP=" + boss.getMaxHp()
                + " ATK=" + boss.getAttackPower() + " DEF=" + boss.getDefense()
                + " (starts in Phase " + boss.getPhase() + " — " + boss.getStrategy().getName() + ")");

        DungeonEngine engine = new DungeonEngine(heroes, boss, publisher);
        EncounterResult result = engine.run();

        System.out.println();
        System.out.println("============================================");
        System.out.println("            Encounter complete");
        System.out.println("   Heroes won:        " + result.isHeroesWon());
        System.out.println("   Rounds played:     " + result.getRoundsPlayed());
        System.out.println("   Surviving heroes:  " + result.getSurvivingHeroes());
        System.out.println("============================================");
    }
}
