package edu.hitsz.aircraftwar.game.repository.object.aircraft.strategy;

import java.util.List;

import edu.hitsz.aircraftwar.game.repository.object.aircraft.AbstractAircraft;
import edu.hitsz.aircraftwar.game.repository.object.bullet.AbstractBullet;

public interface ShootStrategy {
    public List<AbstractBullet> shoot(AbstractAircraft context);
}
