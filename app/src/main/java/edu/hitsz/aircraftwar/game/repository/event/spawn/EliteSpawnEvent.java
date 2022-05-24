package edu.hitsz.aircraftwar.game.repository.event.spawn;

public class EliteSpawnEvent extends SpawnEvent{
    public EliteSpawnEvent(int num, int hp, int power) {
        super(num, hp, power);
    }
}
