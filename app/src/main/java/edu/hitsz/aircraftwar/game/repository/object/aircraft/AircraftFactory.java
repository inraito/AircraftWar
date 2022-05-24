package edu.hitsz.aircraftwar.game.repository.object.aircraft;

import static edu.hitsz.aircraftwar.util.Assert.assertNotNull;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.Map;
import java.util.Random;

import edu.hitsz.aircraftwar.game.view.FlyingObjectView;

public class AircraftFactory {
    public static AircraftFactory instance;

    public static AircraftFactory getInstance(){
        if(instance == null) {
            throw new RuntimeException("AircraftFactory not initialized!");
        }
        return instance;
    }

    private Drawable mobEnemyImage;
    private Drawable eliteEnemyImage;
    private Drawable bossEnemyImage;
    private Drawable heroImage;
    private Rect screenRect;
    private Random random = new Random();

    /**
     * initialize the aircraft factory
     * @param map aircraft's images, index by String names in
     * @see edu.hitsz.aircraftwar.game.view.BackgroundView
     */
    public static void init(Map<String, Drawable> map, Rect screenRect){
        instance = new AircraftFactory(map, screenRect);
    }

    private AircraftFactory(Map<String, Drawable> map, Rect screenRect){
        try{
            random.setSeed(System.nanoTime());
            this.mobEnemyImage = map.get(FlyingObjectView.FlyingObjectKeys.mobImage);
            this.eliteEnemyImage = map.get(FlyingObjectView.FlyingObjectKeys.eliteImage);
            this.bossEnemyImage = map.get(FlyingObjectView.FlyingObjectKeys.bossImage);
            this.heroImage = map.get(FlyingObjectView.FlyingObjectKeys.heroImage);
            this.screenRect = screenRect;
            assertNotNull(mobEnemyImage);
            assertNotNull(eliteEnemyImage);
            assertNotNull(bossEnemyImage);
            assertNotNull(heroImage);
            assertNotNull(this.screenRect);
        }catch(NullPointerException e){
            Log.e("AircraftFactory", "Factory initialization failed!");
            Log.e("AircraftFactory", "Drawable null!");
        }
    }

    private MobEnemy getMobEnemy(int locationX, int locationY, int speedX, int speedY, int hp){
        return new MobEnemy(locationX,locationY,speedX,speedY,hp,screenRect,mobEnemyImage);
    }

    private EliteEnemy getEliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp, int power){
        return new EliteEnemy(locationX,locationY,speedX,speedY,hp,power,screenRect,eliteEnemyImage);
    }

    private BossEnemy getBossEnemy(int locationX, int locationY, int speedX, int speedY, int hp, int power){
        return new BossEnemy(locationX,locationY,speedX,speedY,hp,power,screenRect,bossEnemyImage);
    }

    public HeroAircraft getHeroAircraft(){
        return new HeroAircraft(screenRect.centerX()
                , screenRect.bottom*5/6
                , 0, 0, 1000, 80, screenRect, heroImage);
    }

    public MobEnemy getMobEnemy(int hp){
        return this.getMobEnemy((int) (random.nextDouble() * (screenRect.width() - mobEnemyImage.getIntrinsicWidth())),
                (int) (random.nextDouble() * screenRect.width() * 0.2),
                0,
                10,
                hp);
    }

    public EliteEnemy getEliteEnemy(int hp, int power){
        int xSpeed = (int)(random.nextDouble()*2+1);
        if(random.nextInt()%2==1) {
            xSpeed = -xSpeed;
        }
        return this.getEliteEnemy((int) (random.nextDouble() * (screenRect.width() - eliteEnemyImage.getIntrinsicWidth())),
                (int) (random.nextDouble() * screenRect.width() * 0.2),
                xSpeed,
                10,
                hp,
                power);
    }

    public BossEnemy getBossEnemy(int hp, int power){
        return this.getBossEnemy((int) (random.nextDouble() * (screenRect.width() - bossEnemyImage.getIntrinsicWidth())),
                (int) (random.nextDouble() * screenRect.width() * 0.2),
                5,
                0,
                hp,
                power);
    }
}
