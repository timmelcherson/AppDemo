package com.agile.appdemo.database.entities;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "plan_table")
public class Plan {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "plan_id")
    private int planId;

    @ColumnInfo(name = "plan_name")
    private String planName;

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }
}
