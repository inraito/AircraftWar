package edu.hitsz.aircraftwar.game.repository.object.aircraft;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import java.util.List;

import edu.hitsz.aircraftwar.game.repository.object.aircraft.strategy.ShootStrategy;
import edu.hitsz.aircraftwar.game.repository.object.aircraft.strategy.StraightStrategy;
import edu.hitsz.aircraftwar.game.repository.object.bullet.AbstractBullet;
import edu.hitsz.aircraftwar.game.view.FlyingObjectView;

public class EliteEnemy extends MobEnemy{
    protected ShootStrategy shootStrategy;
    @Override
    public String getImageKey(){
        return FlyingObjectView.FlyingObjectKeys.eliteImage;
    }

    protected EliteEnemy(int locationX, int locationY, int speedX, int speedY,int hp, int power, Rect screenRect, Drawable image) {
        super(locationX, locationY, speedX, speedY, hp, screenRect, image);
        this.shootStrategy = new StraightStrategy(1,2, power);
    }

    @Override
    public List<AbstractBullet> shoot() {
        List<AbstractBullet> list = this.shootStrategy.shoot(this);
        return list;
    }
}
