package edu.hitsz.aircraftwar.rank.repository.dao;

import android.content.Context;

import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import edu.hitsz.aircraftwar.MainMenuActivity;

@Singleton
public class RankRoomSource {
    RankDatabase database;
    RankEntryDao dao;
    MainMenuActivity.Difficulty difficulty;

    @Inject
    public RankRoomSource(){

    }

    public void init(Context context, MainMenuActivity.Difficulty difficulty){
        this.difficulty = difficulty;
        database = Room.databaseBuilder(context, RankDatabase.class, "RankDatabase").build();
        dao = database.rankEntryDao();
    }

    public List<RankEntry> getAllRankEntries(){
        List<RankEntry> lists = new ArrayList<>();
        for(RankEntry entry:this.dao.getAllRankEntries()){
            if(entry.difficulty == this.difficulty){
                lists.add(entry);
            }
        }
        return lists;
    }

    public void deleteRankEntry(RankEntry entry){
        this.dao.deleteRankEntry(entry);
    }

    public void insertRankEntry(RankEntry entry){
        this.dao.insertRankEntry(entry);
    }
}
