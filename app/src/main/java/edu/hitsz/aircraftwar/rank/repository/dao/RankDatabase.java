package edu.hitsz.aircraftwar.rank.repository.dao;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {RankEntry.class}, version = 1)
public abstract class RankDatabase extends RoomDatabase {
    public abstract RankEntryDao rankEntryDao();
}
