package com.narxoz.rpg.combatant;

import com.narxoz.rpg.observer.GameEvent;
import com.narxoz.rpg.observer.GameEventType;
import com.narxoz.rpg.observer.GameObserver;
import com.narxoz.rpg.observer.GamePublisher;
import com.narxoz.rpg.strategy.BerserkStrategy;
import com.narxoz.rpg.strategy.CombatStrategy;
import com.narxoz.rpg.strategy.DesperateStrategy;
import com.narxoz.rpg.strategy.MeasuredStrategy;

public class DungeonBoss implements GameObserver {

    private final String name;
    private int hp;
    private final int maxHp;
    private final int attackPower;
    private final int defense;
    private int phase;
    private CombatStrategy strategy;
    private final GamePublisher publisher;

    private final CombatStrategy phase1Strategy = new MeasuredStrategy();
    private final CombatStrategy phase2Strategy = new BerserkStrategy();
    private final CombatStrategy phase3Strategy = new DesperateStrategy();

    public DungeonBoss(String name, int hp, int attackPower, int defense, GamePublisher publisher) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.attackPower = attackPower;
        this.defense = defense;
        this.phase = 1;
        this.strategy = phase1Strategy;
        this.publisher = publisher;
        publisher.register(this);
    }

    public String getName()              { return name; }
    public int getHp()                   { return hp; }
    public int getMaxHp()                { return maxHp; }
    public int getAttackPower()          { return attackPower; }
    public int getDefense()              { return defense; }
    public int getPhase()                { return phase; }
    public CombatStrategy getStrategy()  { return strategy; }
    public boolean isAlive()             { return hp > 0; }

    public void takeDamage(int amount) {
        if (hp <= 0) return;
        int oldPhase = computePhaseForHp(hp);
        hp = Math.max(0, hp - amount);
        if (hp <= 0) return;
        int newPhase = computePhaseForHp(hp);
        for (int p = oldPhase + 1; p <= newPhase; p++) {
            publisher.fire(new GameEvent(GameEventType.BOSS_PHASE_CHANGED, name, p));
        }
    }

    private int computePhaseForHp(int currentHp) {
        double pct = (double) currentHp / maxHp;
        if (pct >= 0.6) return 1;
        if (pct >= 0.3) return 2;
        return 3;
    }

    @Override
    public void onEvent(GameEvent event) {
        if (event.getType() != GameEventType.BOSS_PHASE_CHANGED) return;
        if (!name.equals(event.getSourceName())) return;

        int shouldBePhase = computePhaseForHp(hp);
        if (shouldBePhase == this.phase) return;

        this.phase = shouldBePhase;
        this.strategy = switch (shouldBePhase) {
            case 2 -> phase2Strategy;
            case 3 -> phase3Strategy;
            default -> phase1Strategy;
        };
    }
}
