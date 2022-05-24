package edu.hitsz.aircraftwar.game.repository.object.aircraft;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.hitsz.aircraftwar.game.repository.object.AbstractFlyingObject;
import edu.hitsz.aircraftwar.game.repository.object.bullet.AbstractBullet;

public abstract class AbstractAircraft extends AbstractFlyingObject {
    protected volatile int hp;
    protected volatile int maxHp;

    /**
     * decrease hp
     * @param decrease the hp to be decrese
     * @return whether or not this aircraft was destroyed by this decrease
     */
    public boolean decreaseHp(int decrease){
        if(hp <= 0) {
            return false;
        }
        hp -= decrease;
        if(hp <= 0){
            hp=0;
            return true;
        }
        return false;
    }

    public int getHp(){
        return this.hp;
    }

    protected AbstractAircraft(int locationX, int locationY, int speedX, int speedY, int hp, Rect screenRect, Drawable image) {
        super(locationX, locationY, speedX, speedY, screenRect, image);
        this.hp = hp;
        this.maxHp = hp;
    }

    public List<AbstractBullet> shoot(){
        return null;
    };
}
