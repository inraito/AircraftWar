package edu.hitsz.aircraftwar.game.repository.difficulty.template;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.hitsz.aircraftwar.game.repository.FlyingObjectFactoryManager;
import edu.hitsz.aircraftwar.game.repository.GameAssetsManager;
import edu.hitsz.aircraftwar.game.repository.GameAudioManager;
import edu.hitsz.aircraftwar.game.repository.GameEventBus;
import edu.hitsz.aircraftwar.game.repository.GameEventBus.GameEventSubscriber;
import edu.hitsz.aircraftwar.game.repository.GameRepository;
import edu.hitsz.aircraftwar.game.repository.event.AircraftKilledEvent;
import edu.hitsz.aircraftwar.game.repository.event.BulletHitEvent;
import edu.hitsz.aircraftwar.game.repository.event.CheckBulletPropTimeoutEvent;
import edu.hitsz.aircraftwar.game.repository.event.CollideCheckEvent;
import edu.hitsz.aircraftwar.game.repository.event.DropPropEvent;
import edu.hitsz.aircraftwar.game.repository.event.FlyingObjectMoveEvent;
import edu.hitsz.aircraftwar.game.repository.event.GameoverEvent;
import edu.hitsz.aircraftwar.game.repository.event.ScoreAddingEvent;
import edu.hitsz.aircraftwar.game.repository.event.shoot.HeroShootEvent;
import edu.hitsz.aircraftwar.game.repository.event.PropGetEvent;
import edu.hitsz.aircraftwar.game.repository.event.shoot.EnemyShootEvent;
import edu.hitsz.aircraftwar.game.repository.event.RemoveInvalidEvent;
import edu.hitsz.aircraftwar.game.repository.event.TickEvent;
import edu.hitsz.aircraftwar.game.repository.event.spawn.BossKilledEvent;
import edu.hitsz.aircraftwar.game.repository.event.spawn.BossSpawnEvent;
import edu.hitsz.aircraftwar.game.repository.event.spawn.EliteSpawnEvent;
import edu.hitsz.aircraftwar.game.repository.event.spawn.MobSpawnEvent;
import edu.hitsz.aircraftwar.game.repository.object.AbstractFlyingObject;
import edu.hitsz.aircraftwar.game.repository.object.aircraft.AbstractAircraft;
import edu.hitsz.aircraftwar.game.repository.object.aircraft.AircraftFactory;
import edu.hitsz.aircraftwar.game.repository.object.aircraft.BossEnemy;
import edu.hitsz.aircraftwar.game.repository.object.aircraft.EliteEnemy;
import edu.hitsz.aircraftwar.game.repository.object.aircraft.HeroAircraft;
import edu.hitsz.aircraftwar.game.repository.object.aircraft.MobEnemy;
import edu.hitsz.aircraftwar.game.repository.object.bullet.AbstractBullet;
import edu.hitsz.aircraftwar.game.repository.object.prop.AbstractProp;
import edu.hitsz.aircraftwar.game.repository.object.prop.BloodProp;
import edu.hitsz.aircraftwar.game.repository.object.prop.BombProp;
import edu.hitsz.aircraftwar.game.repository.object.prop.BulletProp;
import edu.hitsz.aircraftwar.game.repository.object.prop.PropFactory;

public class BaseTemplate {
    protected GameAssetsManager manager;
    protected GameEventBus eventBus;
    protected GameAudioManager audioManager;
    protected Random random = new Random();

    /**
     * used for BulletProp
     */
    private volatile boolean timeout = false;
    private final Object startTimer = new Object();
    private final Thread bulletTimer = new Thread(){
        @Override
        public void run() {
            while(true){
                synchronized (startTimer){
                    try {
                        startTimer.wait();
                    } catch (InterruptedException e) {
                        while(true) {
                            try {
                                startTimer.wait(10000);
                            } catch (InterruptedException exception) {
                                continue;
                            }
                            break;
                        }
                        timeout = true;
                    }
                }
            }
        }
    };

    public BaseTemplate(GameAssetsManager manager, GameEventBus eventBus, GameAudioManager audioManager){
        this.manager = manager;
        this.eventBus = eventBus;
        this.audioManager = audioManager;
    }

    public void init(boolean musicOn){
        random.setSeed(System.nanoTime());
        if(!bulletTimer.isAlive()){
            bulletTimer.start();
        }
        if(musicOn){
            eventBus.subscribe(BossSpawnEvent.class, playBossBgmSubscriber);
            eventBus.subscribe(BossKilledEvent.class, playNormalBgmSubscriber);
            eventBus.subscribe(HeroShootEvent.class, playHeroShootAudioSubscriber);
            eventBus.subscribe(BulletHitEvent.class, playHeroHurtAudioSubscriber);
            eventBus.subscribe(PropGetEvent.class, playBombExplodeAudioSubscriber);
            eventBus.subscribe(GameoverEvent.class, playGameOverAudioSubscriber);
            eventBus.subscribe(PropGetEvent.class, playPropGetAudioSubscriber);
        }

        eventBus.subscribe(TickEvent.class, baseEventGenerator);

        eventBus.subscribe(RemoveInvalidEvent.class, removeInvalidSubscriber);
        eventBus.subscribe(FlyingObjectMoveEvent.class, objectMoveSubscriber);
        eventBus.subscribe(CollideCheckEvent.class, collideCheckSubscriber);
        eventBus.subscribe(CheckBulletPropTimeoutEvent.class, checkBulletPropTimeoutSubscriber);

        eventBus.subscribe(BulletHitEvent.class, bulletHitSubscriber);
        eventBus.subscribe(AircraftKilledEvent.class, aircraftKilledSubscriber);
        eventBus.subscribe(DropPropEvent.class, dropPropSubscriber);
        eventBus.subscribe(PropGetEvent.class, propGetSubscriber);
        eventBus.subscribe(ScoreAddingEvent.class, scoreAddingSubscriber);
        //eventBus.subscribe(GameoverEvent.class, gameoverSubscriber);

        eventBus.subscribe(MobSpawnEvent.class, mobSpawnSubscriber);
        eventBus.subscribe(EliteSpawnEvent.class, eliteSpawnSubscriber);
        eventBus.subscribe(BossSpawnEvent.class, bossSpawnSubscriber);

        eventBus.subscribe(EnemyShootEvent.class, enemyShootSubscriber);
        eventBus.subscribe(HeroShootEvent.class, heroShootSubscriber);
    }

    private final GameEventSubscriber<BossSpawnEvent> playBossBgmSubscriber = new GameEventSubscriber<BossSpawnEvent>() {
        @Override
        public void onEvent(BossSpawnEvent event) {
            audioManager.playBossBgm();
        }
    };

    private final GameEventSubscriber<BossKilledEvent> playNormalBgmSubscriber = new GameEventSubscriber<BossKilledEvent>() {
        @Override
        public void onEvent(BossKilledEvent event) {
            audioManager.playNormalBgm();
        }
    };

    private final GameEventSubscriber<HeroShootEvent> playHeroShootAudioSubscriber = new GameEventSubscriber<HeroShootEvent>() {
        @Override
        public void onEvent(HeroShootEvent event) {
            audioManager.playShootSound();
        }
    };

    private final GameEventSubscriber<BulletHitEvent> playHeroHurtAudioSubscriber = new GameEventSubscriber<BulletHitEvent>() {
        @Override
        public void onEvent(BulletHitEvent event) {
            if(event.getAircraft() instanceof HeroAircraft){
                audioManager.playBulletHitSound();
            }
        }
    };

    private final GameEventSubscriber<PropGetEvent> playBombExplodeAudioSubscriber = new GameEventSubscriber<PropGetEvent>() {
        @Override
        public void onEvent(PropGetEvent event) {
            if(event.getProp() instanceof  BombProp){
                audioManager.playBombExplodeSound();
            }
        }
    };

    private final GameEventSubscriber<GameoverEvent> playGameOverAudioSubscriber = new GameEventSubscriber<GameoverEvent>() {
        @Override
        public void onEvent(GameoverEvent event) {
            audioManager.playGameOverSound();
        }
    };

    private final GameEventSubscriber<PropGetEvent> playPropGetAudioSubscriber = new GameEventSubscriber<PropGetEvent>() {
        @Override
        public void onEvent(PropGetEvent event) {
            audioManager.playSupplyGetSound();
        }
    };

    private final GameEventSubscriber<TickEvent> baseEventGenerator = new GameEventSubscriber<TickEvent>() {
        @Override
        public void onEvent(TickEvent event) {
            eventBus.publish(new FlyingObjectMoveEvent());
            eventBus.publish(new CollideCheckEvent());
            eventBus.publish(new RemoveInvalidEvent());
            eventBus.publish(new CheckBulletPropTimeoutEvent());
        }
    };

    private final GameEventSubscriber<CheckBulletPropTimeoutEvent> checkBulletPropTimeoutSubscriber = new GameEventSubscriber<CheckBulletPropTimeoutEvent>() {
        @Override
        public void onEvent(CheckBulletPropTimeoutEvent event) {
            if(timeout){
                manager.getHeroAircraft().setStraightShoot();
                timeout = false;
            }
        }
    };

    private final GameEventSubscriber<RemoveInvalidEvent> removeInvalidSubscriber = new GameEventSubscriber<RemoveInvalidEvent>() {
        @Override
        public void onEvent(RemoveInvalidEvent event) {
            List<AbstractBullet> enemyBullets = manager.getEnemyBullets();
            List<AbstractBullet> heroBullets = manager.getHeroBullets();
            List<AbstractAircraft> enemyAircraft = manager.getEnemyAircraft();
            List<AbstractProp> props = manager.getProps();
            synchronized (enemyBullets){enemyBullets.removeIf(AbstractFlyingObject::notValid);}
            synchronized (heroBullets){heroBullets.removeIf(AbstractFlyingObject::notValid);}
            synchronized (enemyAircraft){enemyAircraft.removeIf(AbstractFlyingObject::notValid);}
            synchronized (props){props.removeIf(AbstractFlyingObject::notValid);}
        }
    };

    private final GameEventSubscriber<FlyingObjectMoveEvent> objectMoveSubscriber = new GameEventSubscriber<FlyingObjectMoveEvent>() {
        @Override
        public void onEvent(FlyingObjectMoveEvent event) {
            List<AbstractBullet> enemyBullets = manager.getEnemyBullets();
            List<AbstractBullet> heroBullets = manager.getHeroBullets();
            List<AbstractAircraft> enemyAircraft = manager.getEnemyAircraft();
            List<AbstractProp> props = manager.getProps();
            synchronized (enemyBullets){
                for(AbstractBullet bullet:enemyBullets){
                    bullet.forward();
                }
            }
            synchronized (heroBullets){
                for(AbstractBullet bullet:heroBullets){
                    bullet.forward();
                }
            }
            synchronized (enemyAircraft){
                for(AbstractAircraft aircraft:enemyAircraft){
                    aircraft.forward();
                }
            }
            synchronized (props){
                for(AbstractProp prop:props){
                    prop.forward();
                }
            }
        }
    };

    private final GameEventSubscriber<EnemyShootEvent> enemyShootSubscriber = new GameEventSubscriber<EnemyShootEvent>() {
        @Override
        public void onEvent(EnemyShootEvent event) {
            List<AbstractBullet> enemyBullets = manager.getEnemyBullets();
            List<AbstractAircraft> enemyAircraft = manager.getEnemyAircraft();
            synchronized (enemyAircraft){
                for(AbstractAircraft aircraft:enemyAircraft){
                    List<AbstractBullet> bullets = aircraft.shoot();
                    if(bullets!=null){
                        enemyBullets.addAll(bullets);
                    }
                }
            }
        }
    };

    private final GameEventSubscriber<HeroShootEvent> heroShootSubscriber = new GameEventSubscriber<HeroShootEvent>() {
        @Override
        public void onEvent(HeroShootEvent event) {
            manager.getHeroBullets().addAll(manager.getHeroAircraft().shoot());
        }
    };

    private final GameEventSubscriber<CollideCheckEvent> collideCheckSubscriber = new GameEventSubscriber<CollideCheckEvent>() {
        @Override
        public void onEvent(CollideCheckEvent event) {
            List<AbstractBullet> heroBullets = manager.getHeroBullets();
            List<AbstractBullet> enemyBullets = manager.getEnemyBullets();
            HeroAircraft hero = manager.getHeroAircraft();
            List<AbstractAircraft>  enemyAircrafts = manager.getEnemyAircraft();
            List<AbstractProp> props = manager.getProps();
            List<AbstractProp> collidedProps = new ArrayList<>();
            synchronized (props){
                for(AbstractProp prop:props){
                    if(prop.notValid()){
                        continue;
                    }
                    if(prop.isCollide(hero)||hero.isCollide(prop)){
                        collidedProps.add(prop);
                    }
                }
            }
            for(AbstractProp prop:collidedProps){
                eventBus.publish(new PropGetEvent(hero, prop));
            }

            List<AbstractBullet> collidedBullets = new ArrayList<>();
            synchronized (enemyBullets){
                for(AbstractBullet bullet:enemyBullets){
                    if(bullet.notValid()) {
                        continue;
                    }
                    if(bullet.isCollide(hero)||hero.isCollide(bullet)){
                        collidedBullets.add(bullet);
                    }
                }
            }
            for(AbstractBullet bullet:collidedBullets){
                eventBus.publish(new BulletHitEvent(hero, bullet));
            }

            List<BulletHitEvent> events = new ArrayList<>();
            boolean gameOverFlag = false;
            synchronized (heroBullets){
                for (AbstractBullet bullet : heroBullets) {
                    if (bullet.notValid()) {
                        continue;
                    }
                    synchronized (enemyAircrafts) {
                        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                            if (enemyAircraft.notValid()) {
                                continue;
                            }
                            if (enemyAircraft.isCollide(bullet)) {
                                events.add(new BulletHitEvent(enemyAircraft, bullet));
                            }
                            if (enemyAircraft.isCollide(hero) || hero.isCollide(enemyAircraft)) {
                                enemyAircraft.vanish();
                                gameOverFlag = true;
                            }
                        }
                    }
                }
            }
            if(gameOverFlag){
                eventBus.publish(new GameoverEvent());
            }
            for(BulletHitEvent e:events){
                eventBus.publish(e);
            }
        }
    };

    private final GameEventSubscriber<BulletHitEvent> bulletHitSubscriber = new GameEventSubscriber<BulletHitEvent>() {
        @Override
        public void onEvent(BulletHitEvent event) {
            if(event.getAircraft().decreaseHp(event.getBullet().getPower())){
                eventBus.publish(new AircraftKilledEvent(event.getAircraft()));
            }
            event.getBullet().vanish();
        }
    };

    private final GameEventSubscriber<AircraftKilledEvent> aircraftKilledSubscriber = new GameEventSubscriber<AircraftKilledEvent>() {
        @Override
        public void onEvent(AircraftKilledEvent event) {
            AbstractAircraft aircraft = event.getAircraft();
            aircraft.vanish();
            if(aircraft instanceof HeroAircraft){
                eventBus.publish(new GameoverEvent());
            }else if(aircraft instanceof BossEnemy){
                eventBus.publish(new BossKilledEvent());
            }else if(aircraft instanceof EliteEnemy){
                eventBus.publish(new ScoreAddingEvent(25));
            }else if(aircraft instanceof MobEnemy){
                eventBus.publish(new ScoreAddingEvent(10));
            }else{
                throw new RuntimeException("Unknown Aircraft Type!");
            }
        }
    };

    private final GameEventSubscriber<PropGetEvent> propGetSubscriber = new GameEventSubscriber<PropGetEvent>() {
        @Override
        public void onEvent(PropGetEvent event) {
            AbstractProp prop = event.getProp();
            HeroAircraft hero = event.getHero();
            if(prop instanceof BloodProp){
                hero.decreaseHp(-((BloodProp) prop).getHp());
            }else if(prop instanceof BombProp) {
                List<AbstractAircraft> enemies = manager.getEnemyAircraft();
                List<AbstractBullet> bullets = manager.getEnemyBullets();

                List<AircraftKilledEvent> killedEvents = new ArrayList<>();
                synchronized (enemies){
                    for(AbstractAircraft enemy:enemies){
                        if(enemy instanceof BossEnemy){
                            continue;
                        }
                        killedEvents.add(new AircraftKilledEvent(enemy));
                    }
                }
                for(AircraftKilledEvent e:killedEvents){
                    eventBus.publish(e);
                }

                synchronized (bullets){
                    for(AbstractBullet bullet:bullets){
                        bullet.vanish();
                    }
                }
            }else if(prop instanceof BulletProp){
                hero.setSplitShoot();
                bulletTimer.interrupt();
            }else{
                Log.e("BaseTemplate", "Unknown Prop Type!");
                throw new RuntimeException("Fatal Error");
            }
            prop.vanish();
        }
    };

    private final GameEventSubscriber<MobSpawnEvent> mobSpawnSubscriber = new GameEventSubscriber<MobSpawnEvent>() {
        @Override
        public void onEvent(MobSpawnEvent event) {
            List<AbstractAircraft> enemies = manager.getEnemyAircraft();
            AircraftFactory factory = FlyingObjectFactoryManager.getAircraftFactory();
            synchronized (enemies){
                for(int i=0;i<event.getNum();i++){
                    AbstractAircraft enemy = factory.getMobEnemy(event.getHp());
                    enemies.add(enemy);
                }
            }
        }
    };

    private final GameEventSubscriber<EliteSpawnEvent> eliteSpawnSubscriber = new GameEventSubscriber<EliteSpawnEvent>() {
        @Override
        public void onEvent(EliteSpawnEvent event) {
            List<AbstractAircraft> enemies = manager.getEnemyAircraft();
            AircraftFactory factory = FlyingObjectFactoryManager.getAircraftFactory();
            synchronized (enemies){
                for(int i=0;i<event.getNum();i++){
                    AbstractAircraft enemy = factory.getEliteEnemy(event.getHp(), event.getPower());
                    enemies.add(enemy);
                }
            }
        }
    };

    private final GameEventSubscriber<BossSpawnEvent> bossSpawnSubscriber = new GameEventSubscriber<BossSpawnEvent>() {
        @Override
        public void onEvent(BossSpawnEvent event) {
            List<AbstractAircraft> enemies = manager.getEnemyAircraft();
            AircraftFactory factory = FlyingObjectFactoryManager.getAircraftFactory();
            synchronized (enemies){
                for(int i=0;i<event.getNum();i++){
                    AbstractAircraft enemy = factory.getBossEnemy(event.getHp(), event.getPower());
                    enemies.add(enemy);
                }
            }
        }
    };

    private final GameEventSubscriber<ScoreAddingEvent> scoreAddingSubscriber = new GameEventSubscriber<ScoreAddingEvent>() {
        @Override
        public void onEvent(ScoreAddingEvent event) {
            manager.addScore(event.getScore());
        }
    };

    private final GameEventSubscriber<DropPropEvent> dropPropSubscriber = new GameEventSubscriber<DropPropEvent>() {
        @Override
        public void onEvent(DropPropEvent event) {
            PropFactory factory = FlyingObjectFactoryManager.getPropFactory();
            List<AbstractProp> props = manager.getProps();
            props.add(factory.getProp(event.getPropType(), event.getLocationX(), event.getLocationY()));
        }
    };

}
