package com.agile.appdemo.database.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.agile.appdemo.database.entities.Plan;

import java.util.List;

@Dao
public interface PlanDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Plan marker);

    @Update
    void update(Plan marker);

    @Delete
    void delete(Plan marker);

    @Query("SELECT * FROM plan_table")
    LiveData<List<Plan>> observablePlanList();

    @Query("SELECT * FROM plan_table WHERE plan_id = :id")
    Plan getPlan(int id);
}
