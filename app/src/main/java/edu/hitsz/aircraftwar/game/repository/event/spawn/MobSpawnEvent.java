package edu.hitsz.aircraftwar.game.repository.event.spawn;

import edu.hitsz.aircraftwar.game.repository.event.BaseEvent;

public class MobSpawnEvent extends SpawnEvent {
    public MobSpawnEvent(int num, int hp, int power) {
        super(num, hp, power);
    }
}
