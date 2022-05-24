package edu.hitsz.aircraftwar.game.repository.event;

import edu.hitsz.aircraftwar.game.repository.object.aircraft.AbstractAircraft;
import edu.hitsz.aircraftwar.game.repository.object.bullet.AbstractBullet;


public class BulletHitEvent extends BaseEvent{
    /**
     * Guarantee content within is one of the following combination
     * enemy with HeroBullet or hero with EnemyBullet
     */
    private final AbstractAircraft aircraft;
    private final AbstractBullet bullet;

    public BulletHitEvent(AbstractAircraft aircraft, AbstractBullet bullet){
        super();
        this.aircraft = aircraft;
        this.bullet = bullet;
    }

    public AbstractAircraft getAircraft() {
        return aircraft;
    }

    public AbstractBullet getBullet() {
        return bullet;
    }
}
