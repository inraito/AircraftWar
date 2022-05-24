package edu.hitsz.aircraftwar.game.repository.event;

public class ScoreAddingEvent extends BaseEvent{
    private final int score;

    public ScoreAddingEvent(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }
}
