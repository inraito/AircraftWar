package edu.hitsz.aircraftwar.game.repository.event.spawn;

public class BossSpawnEvent extends SpawnEvent {
    public BossSpawnEvent(int num, int hp, int power) {
        super(num, hp, power);
    }
}
