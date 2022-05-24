package edu.hitsz.aircraftwar.game.repository.event;

public class DropPropEvent extends BaseEvent{
    private final Class<?> propType;
    private final int locationX;
    private final int locationY;

    public DropPropEvent(Class<?> propType, int locationX, int locationY) {
        this.propType = propType;
        this.locationX = locationX;
        this.locationY = locationY;
    }

    public Class<?> getPropType() {
        return propType;
    }

    public int getLocationX() {
        return locationX;
    }

    public int getLocationY() {
        return locationY;
    }
}
