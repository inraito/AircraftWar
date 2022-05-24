package edu.hitsz.aircraftwar.game.repository.object.aircraft;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import edu.hitsz.aircraftwar.game.view.FlyingObjectView;

public class MobEnemy extends AbstractAircraft{
    @Override
    public String getImageKey(){
        return FlyingObjectView.FlyingObjectKeys.mobImage;
    }

    protected MobEnemy(int locationX, int locationY, int speedX, int speedY, int hp, Rect screenRect, Drawable image) {
        super(locationX, locationY, speedX, speedY, hp, screenRect, image);
    }
}
