package edu.hitsz.aircraftwar.game.repository.object.bullet;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import edu.hitsz.aircraftwar.game.view.FlyingObjectView;

public class HeroBullet extends AbstractBullet{
    @Override
    public String getImageKey(){
        return FlyingObjectView.FlyingObjectKeys.heroBulletImage;
    }

    protected HeroBullet(int locationX, int locationY, int speedX, int speedY, int power, Rect screenRect, Drawable image) {
        super(locationX, locationY, speedX, speedY, power, screenRect, image);
    }
}
