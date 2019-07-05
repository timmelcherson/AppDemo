package com.agile.appdemo.database.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.agile.appdemo.database.entities.CustomMarker;

import java.util.List;

@Dao
public interface CustomMarkerDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(CustomMarker marker);

    @Update
    void update(CustomMarker marker);

    @Delete
    void delete(CustomMarker marker);

    @Query("SELECT * FROM custom_marker_table")
    LiveData<List<CustomMarker>> observableCustomMarkerList();

    @Query("SELECT * FROM custom_marker_table WHERE custom_marker_id = :id")
    CustomMarker getCustomMarker(String id);
}
