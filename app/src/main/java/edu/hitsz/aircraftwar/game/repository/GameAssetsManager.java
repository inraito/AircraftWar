package edu.hitsz.aircraftwar.game.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import edu.hitsz.aircraftwar.game.repository.object.AbstractFlyingObject;
import edu.hitsz.aircraftwar.game.repository.object.aircraft.AbstractAircraft;
import edu.hitsz.aircraftwar.game.repository.object.aircraft.AircraftFactory;
import edu.hitsz.aircraftwar.game.repository.object.aircraft.HeroAircraft;
import edu.hitsz.aircraftwar.game.repository.object.bullet.AbstractBullet;
import edu.hitsz.aircraftwar.game.repository.object.prop.AbstractProp;

/**
 * Runtime manager of the in-game object
 */
@Singleton
public class GameAssetsManager {
    private volatile int score = 0;
    private volatile HeroAircraft heroAircraft;
    //any changes on this list should be wrapped in a synchronized block
    private final List<AbstractAircraft> enemyAircraft = Collections.synchronizedList(new ArrayList<>());
    private final List<AbstractBullet> heroBullets = Collections.synchronizedList(new ArrayList<>());
    private final List<AbstractBullet> enemyBullets = Collections.synchronizedList(new ArrayList<>());
    private final List<AbstractProp> props = Collections.synchronizedList(new ArrayList<>());

    @Inject
    public GameAssetsManager(){

    }

    public void checkValidity(){
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircraft.removeIf(AbstractFlyingObject::notValid);
        props.removeIf(AbstractFlyingObject::notValid);
    }

    public void flush(){
        score = 0;
        heroAircraft = null;
        enemyAircraft.clear();
        heroBullets.clear();
        enemyBullets.clear();
        props.clear();
    }

    public void addScore(int score){
        this.score += score;
    }

    public int getScore(){
        return this.score;
    }

    public List<AbstractAircraft> getEnemyAircraft() {
        return enemyAircraft;
    }

    public List<AbstractBullet> getHeroBullets() {
        return heroBullets;
    }

    public List<AbstractBullet> getEnemyBullets() {
        return enemyBullets;
    }

    public List<AbstractProp> getProps() {
        return props;
    }

    private Object heroAircraftLock = new Object();
    public HeroAircraft getHeroAircraft(){
        if(heroAircraft == null) {
            synchronized (heroAircraftLock) {
                if (heroAircraft == null) {
                    heroAircraft = AircraftFactory.getInstance().getHeroAircraft();
                }
            }
        }
        return heroAircraft;
    }
}
