package com.agile.appdemo.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "custom_marker_table", foreignKeys = {
        @ForeignKey(entity = Plan.class,
                parentColumns = "plan_id",
                childColumns = "plan_id",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE)
})
public class CustomMarker {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "custom_marker_id")
    private String markerId;

    @NonNull
    @ColumnInfo(name = "plan_id")
    private int planId;

    @ColumnInfo(name = "custom_marker_xcoord")
    private int markerXCoord;

    @ColumnInfo(name = "custom_marker_ycoord")
    private int markerYCoord;

    @NonNull
    public String getMarkerId() {
        return markerId;
    }

    public void setMarkerId(@NonNull String markerId) {
        this.markerId = markerId;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public int getMarkerXCoord() {
        return markerXCoord;
    }

    public void setMarkerXCoord(int markerXCoord) {
        this.markerXCoord = markerXCoord;
    }

    public int getMarkerYCoord() {
        return markerYCoord;
    }

    public void setMarkerYCoord(int markerYCoord) {
        this.markerYCoord = markerYCoord;
    }
}
