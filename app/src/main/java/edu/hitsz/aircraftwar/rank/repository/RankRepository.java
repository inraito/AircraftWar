package edu.hitsz.aircraftwar.rank.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ConcurrentModificationException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import edu.hitsz.aircraftwar.MainMenuActivity;
import edu.hitsz.aircraftwar.rank.repository.dao.RankEntry;
import edu.hitsz.aircraftwar.rank.repository.dao.RankRoomSource;

@Singleton
public class RankRepository {
    private final RankRoomSource source;
    private final MutableLiveData<List<RankEntry>> liveData = new MutableLiveData<>();

    @Inject
    public RankRepository(RankRoomSource source){
        this.source = source;
    }

    public void init(Context context, MainMenuActivity.Difficulty difficulty){
        source.init(context, difficulty);
        new Thread(()->{
            updateLiveData();
        }).start();
    }

    private void updateLiveData() {
        liveData.postValue(source.getAllRankEntries());
    }

    public void deleteRankEntry(RankEntry entry){
        new Thread(()->{
            this.source.deleteRankEntry(entry);
            updateLiveData();
        }).start();
    }

    public void deleteRankEntry(List<RankEntry> entry){
        new Thread(()->{
            for(RankEntry entry1:entry){
                this.source.deleteRankEntry(entry1);
            }
            updateLiveData();
        }).start();
    }

    public void insertRankEntry(RankEntry entry){
        new Thread(()->{
            this.source.insertRankEntry(entry);
            updateLiveData();
        }).start();
    }

    public LiveData<List<RankEntry>> getRankEntryLiveData(){
        return this.liveData;
    }
}
