package edu.hitsz.aircraftwar.game.repository.object.bullet;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.Map;

import edu.hitsz.aircraftwar.game.view.FlyingObjectView;

import static edu.hitsz.aircraftwar.util.Assert.*;

public class BulletFactory {
    public static BulletFactory instance;

    public static BulletFactory getInstance(){
        if(instance == null) {
            throw new RuntimeException("BulletFactory not initialized!");
        }
        return instance;
    }

    private Drawable enemyBulletImage;
    private Drawable heroBulletImage;
    private Rect screenRect;

    /**
     * initialize the bullet factory
     * @param map bullets' images, index by String names in
     * @see edu.hitsz.aircraftwar.game.view.BackgroundView
     */
    public static void init(Map<String, Drawable> map, Rect screenRect){
        instance = new BulletFactory(map, screenRect);
    }

    private BulletFactory(Map<String, Drawable> map, Rect screenRect){
        try{
            this.enemyBulletImage = map.get(FlyingObjectView.FlyingObjectKeys.enemyBulletImage);
            this.heroBulletImage = map.get(FlyingObjectView.FlyingObjectKeys.heroBulletImage);
            this.screenRect = screenRect;
            assertNotNull(enemyBulletImage);
            assertNotNull(heroBulletImage);
            assertNotNull(this.screenRect);
        }catch(NullPointerException e){
            Log.e("BulletFactory", "Factory initialization failed!");
            Log.e("BulletFactory", "Drawable null!");
        }
    }

    public EnemyBullet getEnemyBullet(int locationX, int locationY, int speedX, int speedY, int power){
        return new EnemyBullet(locationX,locationY,speedX,speedY, power,screenRect,enemyBulletImage);
    }

    public HeroBullet getHeroBullet(int locationX, int locationY, int speedX, int speedY, int power){
        return new HeroBullet(locationX,locationY,speedX,speedY, power,screenRect,heroBulletImage);
    }
}
