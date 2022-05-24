package edu.hitsz.aircraftwar.game.repository.event;

import edu.hitsz.aircraftwar.game.repository.object.aircraft.AbstractAircraft;
import edu.hitsz.aircraftwar.game.repository.object.aircraft.HeroAircraft;
import edu.hitsz.aircraftwar.game.repository.object.prop.AbstractProp;

public class PropGetEvent extends BaseEvent{
    private final HeroAircraft hero;
    private final AbstractProp prop;

    public PropGetEvent(HeroAircraft hero, AbstractProp prop){
        this.hero = hero;
        this.prop = prop;
    }

    public HeroAircraft getHero() {
        return hero;
    }

    public AbstractProp getProp() {
        return prop;
    }
}
