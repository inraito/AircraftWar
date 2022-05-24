package edu.hitsz.aircraftwar.rank.repository.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface RankEntryDao {
    @Insert
    public void insertRankEntry(RankEntry entry);

    @Delete
    public void deleteRankEntry(RankEntry entry);

    @Query("SELECT * FROM RankEntry")
    public RankEntry[] getAllRankEntries();
}
