package com.narxoz.rpg.engine;

import com.narxoz.rpg.combatant.DungeonBoss;
import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.observer.GameEvent;
import com.narxoz.rpg.observer.GameEventType;
import com.narxoz.rpg.observer.GamePublisher;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DungeonEngine {

    private static final int MAX_ROUNDS = 50;
    private static final double LOW_HP_THRESHOLD = 0.30;

    private final List<Hero> heroes;
    private final DungeonBoss boss;
    private final GamePublisher publisher;
    private final Set<String> heroLowHpFired = new HashSet<>();

    public DungeonEngine(List<Hero> heroes, DungeonBoss boss, GamePublisher publisher) {
        this.heroes = heroes;
        this.boss = boss;
        this.publisher = publisher;
    }

    public EncounterResult run() {
        int round = 0;
        while (round < MAX_ROUNDS) {
            round++;
            System.out.println();
            System.out.println("=== Round " + round + " (Boss phase " + boss.getPhase()
                    + " — " + boss.getStrategy().getName() + ") ===");

            for (Hero hero : heroes) {
                if (!boss.isAlive()) break;
                if (!hero.isAlive()) continue;
                heroAttack(hero);
            }

            if (!boss.isAlive()) {
                publisher.fire(new GameEvent(GameEventType.BOSS_DEFEATED, boss.getName(), round));
                return new EncounterResult(true, round, countLiving());
            }

            for (Hero hero : heroes) {
                if (!hero.isAlive()) continue;
                bossAttack(hero);
            }

            if (countLiving() == 0) {
                return new EncounterResult(false, round, 0);
            }
        }
        return new EncounterResult(false, MAX_ROUNDS, countLiving());
    }

    private void heroAttack(Hero hero) {
        int raw = hero.getStrategy().calculateDamage(hero.getAttackPower())
                - boss.getStrategy().calculateDefense(boss.getDefense());
        int dmg = Math.max(1, raw);
        publisher.fire(new GameEvent(GameEventType.ATTACK_LANDED, hero.getName(), dmg));
        boss.takeDamage(dmg);
    }

    private void bossAttack(Hero hero) {
        int raw = boss.getStrategy().calculateDamage(boss.getAttackPower())
                - hero.getStrategy().calculateDefense(hero.getDefense());
        int dmg = Math.max(1, raw);
        publisher.fire(new GameEvent(GameEventType.ATTACK_LANDED, boss.getName(), dmg));
        hero.takeDamage(dmg);

        if (hero.isAlive()
                && hero.getHp() < hero.getMaxHp() * LOW_HP_THRESHOLD
                && heroLowHpFired.add(hero.getName())) {
            publisher.fire(new GameEvent(GameEventType.HERO_LOW_HP, hero.getName(), hero.getHp()));
        }
        if (!hero.isAlive()) {
            publisher.fire(new GameEvent(GameEventType.HERO_DIED, hero.getName(), 0));
        }
    }

    private int countLiving() {
        int count = 0;
        for (Hero hero : heroes) {
            if (hero.isAlive()) count++;
        }
        return count;
    }
}
