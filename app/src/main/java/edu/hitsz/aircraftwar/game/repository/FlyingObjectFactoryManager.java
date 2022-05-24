package edu.hitsz.aircraftwar.game.repository;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import java.util.Map;

import edu.hitsz.aircraftwar.game.repository.object.aircraft.AircraftFactory;
import edu.hitsz.aircraftwar.game.repository.object.bullet.BulletFactory;
import edu.hitsz.aircraftwar.game.repository.object.prop.PropFactory;

/**
 * Manager of all factories of FlyingObject
 */
public class FlyingObjectFactoryManager {

    /**
     * initialize the factories
     * @param map FlyingObjects' images, index by String names in
     * @see edu.hitsz.aircraftwar.game.view.BackgroundView
     */
    public static void init(Map<String, Drawable> map, Rect screenRect){
        AircraftFactory.init(map, screenRect);
        BulletFactory.init(map, screenRect);
        PropFactory.init(map, screenRect);
    }

    public static AircraftFactory getAircraftFactory(){
        return AircraftFactory.getInstance();
    }
    public static BulletFactory getBulletFactory(){
        return BulletFactory.getInstance();
    }
    public static PropFactory getPropFactory(){
        return PropFactory.getInstance();
    }

}
