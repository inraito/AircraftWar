package edu.hitsz.aircraftwar.game.repository.event;

import edu.hitsz.aircraftwar.game.repository.object.aircraft.AbstractAircraft;

public class AircraftKilledEvent extends BaseEvent{
    private final AbstractAircraft aircraft;

    public AircraftKilledEvent(AbstractAircraft aircraft){
        this.aircraft = aircraft;
    }

    public AbstractAircraft getAircraft() {
        return aircraft;
    }
}
