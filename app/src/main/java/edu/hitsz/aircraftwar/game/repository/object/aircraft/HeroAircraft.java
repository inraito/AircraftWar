package edu.hitsz.aircraftwar.game.repository.object.aircraft;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import java.util.List;

import edu.hitsz.aircraftwar.game.repository.object.aircraft.strategy.ShootStrategy;
import edu.hitsz.aircraftwar.game.repository.object.aircraft.strategy.SplitStrategy;
import edu.hitsz.aircraftwar.game.repository.object.aircraft.strategy.StraightStrategy;
import edu.hitsz.aircraftwar.game.repository.object.bullet.AbstractBullet;
import edu.hitsz.aircraftwar.game.view.FlyingObjectView;

public class HeroAircraft extends AbstractAircraft{
    private ShootStrategy shootStrategy;
    private int power;
    @Override
    public String getImageKey(){
        return FlyingObjectView.FlyingObjectKeys.heroImage;
    }

    @Override
    public void forward(){
        throw new RuntimeException("HeroAircraft should not use forward()!");
    }

    public void setLocation(int x, int y){
        this.locationX = x;
        this.locationY = y;
    }

    protected HeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp, int power, Rect screenRect, Drawable image) {
        super(locationX, locationY, speedX, speedY, hp, screenRect, image);
        this.power = power;
        this.shootStrategy = new StraightStrategy(-1,2, power);
    }

    @Override
    public List<AbstractBullet> shoot() {
        List<AbstractBullet> list = this.shootStrategy.shoot(this);
        return list;
    }

    public void setStraightShoot(){
        if(!(this.shootStrategy instanceof StraightStrategy)){
            this.shootStrategy = new StraightStrategy(-1,2, power);
        }
    }

    public void setSplitShoot(){
        if(!(this.shootStrategy instanceof SplitStrategy)){
            this.shootStrategy = new SplitStrategy(-1,3, power);
        }
    }
}
