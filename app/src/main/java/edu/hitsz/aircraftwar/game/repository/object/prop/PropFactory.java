package edu.hitsz.aircraftwar.game.repository.object.prop;

import static edu.hitsz.aircraftwar.util.Assert.assertNotNull;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.Map;

import edu.hitsz.aircraftwar.game.view.FlyingObjectView;

public class PropFactory {
    public static PropFactory instance;

    public static PropFactory getInstance(){
        if(instance == null) {
            throw new RuntimeException("PropFactory not initialized!");
        }
        return instance;
    }

    private Drawable bloodPropImage;
    private Drawable bombPropImage;
    private Drawable bulletPropImage;
    private Rect screenRect;

    /**
     * initialize the prop factory
     * @param map props' images, index by String names in
     * @see edu.hitsz.aircraftwar.game.view.BackgroundView
     */
    public static void init(Map<String, Drawable> map, Rect screenRect){
        instance = new PropFactory(map, screenRect);
    }

    private PropFactory(Map<String, Drawable> map, Rect screenRect){
        try{
            this.bloodPropImage = map.get(FlyingObjectView.FlyingObjectKeys.bloodPropImage);
            this.bombPropImage = map.get(FlyingObjectView.FlyingObjectKeys.bombImage);
            this.bulletPropImage = map.get(FlyingObjectView.FlyingObjectKeys.bulletPropImage);
            this.screenRect = screenRect;
            assertNotNull(bloodPropImage);
            assertNotNull(bombPropImage);
            assertNotNull(bulletPropImage);
            assertNotNull(this.screenRect);
        }catch(NullPointerException e){
            Log.e("PropFactory", "Factory initialization failed!");
            Log.e("PropFactory", "Drawable null!");
            throw new RuntimeException("Drawable null!");
        }
    }

    private BloodProp getBloodProp(int locationX, int locationY){
        return new BloodProp(locationX,locationY,screenRect,bloodPropImage);
    }

    private BombProp getBombProp(int locationX, int locationY){
        return new BombProp(locationX,locationY,screenRect,bombPropImage);
    }

    private BulletProp getBulletProp(int locationX, int locationY){
        return new BulletProp(locationX,locationY,screenRect,bulletPropImage);
    }

    public AbstractProp getProp(Class<?> propType, int locationX, int locationY){
        if (propType == BloodProp.class){
            return this.getBloodProp(locationX, locationY);
        } else if (propType == BombProp.class){
            return this.getBombProp(locationX, locationY);
        } else if (propType == BulletProp.class){
            return this.getBulletProp(locationX, locationY);
        } else {
            throw new RuntimeException("Unknown Prop Type!");
        }
    }
}
