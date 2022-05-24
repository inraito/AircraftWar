package edu.hitsz.aircraftwar.rank;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Database;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import edu.hitsz.aircraftwar.MainMenuActivity;
import edu.hitsz.aircraftwar.rank.repository.RankRepository;
import edu.hitsz.aircraftwar.rank.repository.dao.RankEntry;
import kotlin.random.RandomKt;

@HiltViewModel
public class RankViewModel extends ViewModel {
    private final RankRepository repository;

    @Inject
    public RankViewModel(RankRepository repository){
        this.repository = repository;
    }

    public void init(Context context, MainMenuActivity.Difficulty difficulty){
        repository.init(context, difficulty);
    }

    public LiveData<List<RankEntry>> getRankEntryLiveData(){
        return repository.getRankEntryLiveData();
    }

    public void InsertRankEntry(RankEntry entry){
        this.repository.insertRankEntry(entry);
    }

    public void DeleteRankEntry(RankEntry entry){
        this.repository.deleteRankEntry(entry);
    }

    public void DeleteRankEntry(List<RankEntry> entry){
        this.repository.deleteRankEntry(entry);
    }

}
