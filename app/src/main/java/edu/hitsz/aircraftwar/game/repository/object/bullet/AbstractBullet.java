package edu.hitsz.aircraftwar.game.repository.object.bullet;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import edu.hitsz.aircraftwar.game.repository.object.AbstractFlyingObject;

public abstract class AbstractBullet extends AbstractFlyingObject {
    private volatile int power = 10;

    @Override
    public void forward() {
        super.forward();
        if (locationX <= 0 || locationX >= screenRect.width()) {
            vanish();
        }
        if (speedY > 0 && locationY >= screenRect.height() ) {
            vanish();
        }else if (locationY <= 0){
            vanish();
        }
    }

    public int getPower() {
        return power;
    }

    protected AbstractBullet(int locationX, int locationY, int speedX, int speedY, int power, Rect screenRect, Drawable image) {
        super(locationX, locationY, speedX, speedY, screenRect, image);
        this.power = power;
    }
}
