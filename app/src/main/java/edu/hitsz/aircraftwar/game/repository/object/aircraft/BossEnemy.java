package edu.hitsz.aircraftwar.game.repository.object.aircraft;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import edu.hitsz.aircraftwar.game.repository.object.aircraft.strategy.SplitStrategy;
import edu.hitsz.aircraftwar.game.view.FlyingObjectView;

public class BossEnemy extends EliteEnemy{
    @Override
    public String getImageKey(){
        return FlyingObjectView.FlyingObjectKeys.bossImage;
    }

    protected BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp, int power, Rect screenRect, Drawable image) {
        super(locationX, locationY, speedX, speedY, hp, power, screenRect, image);
        this.shootStrategy = new SplitStrategy(1,3, power);
    }
}
