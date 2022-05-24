package edu.hitsz.aircraftwar.rank.repository.dao;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import edu.hitsz.aircraftwar.MainMenuActivity;

@Entity
public class RankEntry {
    @PrimaryKey
    public long timeStamp;

    public int score;
    public String username;
    MainMenuActivity.Difficulty difficulty;

    public int getScore(){
        return this.score;
    }

    public RankEntry(long timeStamp, int score, String username, MainMenuActivity.Difficulty difficulty){
        this.timeStamp = timeStamp;
        this.score = score;
        this.username = username;
        this.difficulty = difficulty;
    }
}
