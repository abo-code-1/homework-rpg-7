package com.narxoz.rpg.observer.impl;

import com.narxoz.rpg.observer.GameEvent;
import com.narxoz.rpg.observer.GameObserver;

public class BattleLogger implements GameObserver {

    @Override
    public void onEvent(GameEvent event) {
        switch (event.getType()) {
            case ATTACK_LANDED ->
                System.out.println("[LOG] " + event.getSourceName() + " lands a hit for " + event.getValue() + " damage.");
            case HERO_LOW_HP ->
                System.out.println("[LOG] " + event.getSourceName() + " is critically wounded (HP=" + event.getValue() + ")!");
            case HERO_DIED ->
                System.out.println("[LOG] " + event.getSourceName() + " has fallen in battle.");
            case BOSS_PHASE_CHANGED ->
                System.out.println("[LOG] " + event.getSourceName() + " enters phase " + event.getValue() + ".");
            case BOSS_DEFEATED ->
                System.out.println("[LOG] " + event.getSourceName() + " has been defeated after " + event.getValue() + " rounds!");
        }
    }
}
