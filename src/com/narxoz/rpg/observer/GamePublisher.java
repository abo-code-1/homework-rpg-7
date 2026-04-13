package com.narxoz.rpg.observer;

import java.util.ArrayList;
import java.util.List;

public class GamePublisher {

    private final List<GameObserver> observers = new ArrayList<>();

    public void register(GameObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void unregister(GameObserver observer) {
        observers.remove(observer);
    }

    public void fire(GameEvent event) {
        if (event == null) {
            return;
        }
        for (GameObserver observer : new ArrayList<>(observers)) {
            observer.onEvent(event);
        }
    }
}
