package edu.hitsz.aircraftwar.game.repository.object.prop;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import edu.hitsz.aircraftwar.game.repository.object.AbstractFlyingObject;
import edu.hitsz.aircraftwar.game.repository.object.aircraft.HeroAircraft;

public abstract class AbstractProp extends AbstractFlyingObject {
    protected AbstractProp(int locationX, int locationY, int speedX, int speedY, Rect screenRect, Drawable image) {
        super(locationX, locationY, speedX, speedY, screenRect, image);
    }

    protected AbstractProp(int locationX, int locationY, Rect screenRect, Drawable image){
        this(locationX, locationY, 0, 10, screenRect, image);
    }

    @Override
    public void forward() {
        super.forward();
        if (locationX <= 0 || locationX >= this.screenRect.width()) {
            vanish();
        }
        if (speedY > 0 && locationY >= this.screenRect.height() ) {
            vanish();
        }else if (locationY <= 0){
            vanish();
        }
    }
}
