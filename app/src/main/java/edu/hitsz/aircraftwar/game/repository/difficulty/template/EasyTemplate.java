package edu.hitsz.aircraftwar.game.repository.difficulty.template;

import javax.inject.Inject;
import javax.inject.Singleton;

import edu.hitsz.aircraftwar.game.repository.GameAssetsManager;
import edu.hitsz.aircraftwar.game.repository.GameAudioManager;
import edu.hitsz.aircraftwar.game.repository.GameEventBus;
import edu.hitsz.aircraftwar.game.repository.GameEventBus.GameEventSubscriber;
import edu.hitsz.aircraftwar.game.repository.event.AircraftKilledEvent;
import edu.hitsz.aircraftwar.game.repository.event.DropPropEvent;
import edu.hitsz.aircraftwar.game.repository.event.TickEvent;
import edu.hitsz.aircraftwar.game.repository.event.shoot.EnemyShootEvent;
import edu.hitsz.aircraftwar.game.repository.event.shoot.HeroShootEvent;
import edu.hitsz.aircraftwar.game.repository.event.spawn.EliteSpawnEvent;
import edu.hitsz.aircraftwar.game.repository.event.spawn.MobSpawnEvent;
import edu.hitsz.aircraftwar.game.repository.object.aircraft.AbstractAircraft;
import edu.hitsz.aircraftwar.game.repository.object.aircraft.EliteEnemy;
import edu.hitsz.aircraftwar.game.repository.object.prop.BloodProp;
import edu.hitsz.aircraftwar.game.repository.object.prop.BombProp;
import edu.hitsz.aircraftwar.game.repository.object.prop.BulletProp;

import static edu.hitsz.aircraftwar.game.repository.StaticField.*;

import java.util.List;

@Singleton
public class EasyTemplate extends BaseTemplate{
    @Inject
    public EasyTemplate(GameAssetsManager manager, GameEventBus eventBus, GameAudioManager audioManager) {
        super(manager, eventBus, audioManager);
    }

    @Override
    public void init(boolean musicOn){
        super.init(musicOn);
        eventBus.subscribe(AircraftKilledEvent.class, dropPropSubscriber);
        eventBus.subscribe(TickEvent.class, spawnEventGenerator);
        eventBus.subscribe(TickEvent.class, shootEventGenerator);
    }

    private final GameEventSubscriber<AircraftKilledEvent> dropPropSubscriber = new GameEventSubscriber<AircraftKilledEvent>() {
        @Override
        public void onEvent(AircraftKilledEvent event) {
            AbstractAircraft aircraft = event.getAircraft();
            if(aircraft instanceof EliteEnemy){
                int rand = random.nextInt()%10;
                if(rand<=2){
                    eventBus.publish(new DropPropEvent(BloodProp.class, aircraft.getLocationX(), aircraft.getLocationY()));
                }else if(rand<=5){
                    eventBus.publish(new DropPropEvent(BombProp.class, aircraft.getLocationX(), aircraft.getLocationY()));
                }else if(rand<=8){
                    eventBus.publish(new DropPropEvent(BulletProp.class, aircraft.getLocationX(), aircraft.getLocationY()));
                }
            }
        }
    };

    private final int mobSpawnThreshold = 900;
    private int mobSpawnCount = 0;
    private final int eliteSpawnThreshold = 1500;
    private int eliteSpawnCount = 0;
    private final GameEventSubscriber<TickEvent> spawnEventGenerator = new GameEventSubscriber<TickEvent>() {
        @Override
        public void onEvent(TickEvent event) {
            mobSpawnCount += timeInterval;
            eliteSpawnCount += timeInterval;
            if(mobSpawnCount >= mobSpawnThreshold){
                mobSpawnCount %= mobSpawnThreshold;
                eventBus.publish(new MobSpawnEvent(1,50, 0));
            }
            if(eliteSpawnCount >= eliteSpawnThreshold){
                eliteSpawnCount %= eliteSpawnThreshold;
                eventBus.publish(new EliteSpawnEvent(1, 50 ,20));
            }
        }
    };

    private final int heroShootThreshold = 800;
    private int heroShootCount = 0;
    private final int enemyShootThreshold = 800;
    private int enemyShootCount = 0;
    private final GameEventSubscriber<TickEvent> shootEventGenerator = new GameEventSubscriber<TickEvent>() {
        @Override
        public void onEvent(TickEvent event) {
            heroShootCount += timeInterval;
            enemyShootCount += timeInterval;
            if(heroShootCount >= heroShootThreshold){
                heroShootCount %= heroShootThreshold;
                eventBus.publish(new HeroShootEvent());
            }
            if(enemyShootCount >= enemyShootThreshold){
                enemyShootCount %= enemyShootThreshold;
                eventBus.publish(new EnemyShootEvent());
            }
        }
    };
}
