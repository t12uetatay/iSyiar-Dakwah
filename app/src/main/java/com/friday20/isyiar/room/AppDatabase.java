package com.friday20.isyiar.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.friday20.isyiar.model.EntityFollow;
import com.friday20.isyiar.model.EntityLike;
import com.friday20.isyiar.model.EntitySyiar;

@Database(entities = {EntitySyiar.class, EntityLike.class, EntityFollow.class}, version = 1)
//@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract DBOpertion dbOpertion();
    private static AppDatabase INSTANCE;
    private static String DATABASE_NAME="app_data.db";

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DATABASE_NAME)
                            .allowMainThreadQueries()
                            .build();

                }
            }
        }
        return INSTANCE;
    }
}
