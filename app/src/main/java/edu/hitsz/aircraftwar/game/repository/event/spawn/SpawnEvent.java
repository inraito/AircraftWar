package edu.hitsz.aircraftwar.game.repository.event.spawn;

import android.util.Log;

import edu.hitsz.aircraftwar.game.repository.event.BaseEvent;

public class SpawnEvent extends BaseEvent {
    private final int num;
    private final int hp;
    private final int power;

    protected SpawnEvent(int num, int hp, int power) {
        this.num = num;
        this.hp = hp;
        this.power = power;
    }

    public int getNum() {
        return num;
    }

    public int getHp() {
        return hp;
    }

    public int getPower() {
        return power;
    }
}
