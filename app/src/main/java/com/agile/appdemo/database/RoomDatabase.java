package com.agile.appdemo.database;


import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.agile.appdemo.database.dao.CustomMarkerDao;
import com.agile.appdemo.database.dao.PlanDao;
import com.agile.appdemo.database.entities.CustomMarker;
import com.agile.appdemo.database.entities.Plan;

@Database(entities = {CustomMarker.class, Plan.class}, version = 1, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class RoomDatabase extends androidx.room.RoomDatabase {

    public abstract CustomMarkerDao CustomMarkerDao();
    public abstract PlanDao PlanDao();

    private static volatile RoomDatabase INSTANCE;
    private static final String TAG = "TAG";

    public static RoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RoomDatabase.class, "Database")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .addCallback(sDatabaseCallback)
                            .build();

                    Log.d(TAG, "Database constructed");
                }
            }
        }
        return INSTANCE;
    }

    private static androidx.room.RoomDatabase.Callback sDatabaseCallback =
            new androidx.room.RoomDatabase.Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                }
            };
}
