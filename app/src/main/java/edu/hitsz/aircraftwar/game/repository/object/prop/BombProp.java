package edu.hitsz.aircraftwar.game.repository.object.prop;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import edu.hitsz.aircraftwar.game.repository.object.aircraft.HeroAircraft;
import edu.hitsz.aircraftwar.game.view.FlyingObjectView;

public class BombProp extends AbstractProp{

    protected BombProp(int locationX, int locationY, Rect screenRect, Drawable image) {
        super(locationX, locationY, screenRect, image);
    }

    @Override
    public String getImageKey(){
        return FlyingObjectView.FlyingObjectKeys.bombImage;
    }

}
