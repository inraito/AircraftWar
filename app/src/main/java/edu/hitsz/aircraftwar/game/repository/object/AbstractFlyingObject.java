package edu.hitsz.aircraftwar.game.repository.object;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import edu.hitsz.aircraftwar.game.repository.object.aircraft.AbstractAircraft;

public abstract class AbstractFlyingObject {
    protected Rect screenRect;

    protected volatile int locationX;
    protected volatile int locationY;
    protected volatile int speedX;
    protected volatile int speedY;
    protected volatile int height;
    protected volatile int width;
    protected volatile boolean isValid = true;

    public int getLocationX() {
        return locationX;
    }

    public int getLocationY() {
        return locationY;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getSpeedX() {
        return speedX;
    }

    public int getSpeedY() {
        return speedY;
    }

    protected AbstractFlyingObject(int locationX, int locationY, int speedX, int speedY,
                                   Rect screenRect,
                                   Drawable image){
        this.locationX = locationX;
        this.locationY = locationY;
        this.speedX = speedX;
        this.speedY = speedY;
        this.screenRect = screenRect;
        this.height = image.getIntrinsicHeight();
        this.width = image.getIntrinsicWidth();
    }

    public void forward() {
        locationX += speedX;
        locationY += speedY;
        if (locationX <= 0 || locationX >= screenRect.right) {
            speedX = -speedX;
        }
    }

    public boolean isCollide(AbstractFlyingObject abstractFlyingObject){
        int factor = this instanceof AbstractAircraft ? 2 : 1;
        int fFactor = abstractFlyingObject instanceof AbstractAircraft ? 2 : 1;

        int x = abstractFlyingObject.getLocationX();
        int y = abstractFlyingObject.getLocationY();
        int fWidth = abstractFlyingObject.getWidth();
        int fHeight = abstractFlyingObject.getHeight();

        return x + (fWidth+this.getWidth())/2 > locationX
                && x - (fWidth+this.getWidth())/2 < locationX
                && y + ( fHeight/fFactor+this.getHeight()/factor )/2 > locationY
                && y - ( fHeight/fFactor+this.getHeight()/factor )/2 < locationY;
    }

    public boolean isInside(float x, float y){
        return (x > locationX - (float)width / 2) &&
                (x < locationX + (float)width / 2) &&
                (y > locationY - (float)height / 2) &&
                (y < locationY + (float)height / 2);
    }

    public boolean notValid() {
        return !this.isValid;
    }

    public void vanish() {
        isValid = false;
    }

    public String getImageKey(){
        throw new RuntimeException();
    }
}
