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

    @ColumnInfo(name = "custom_marker_image_res")
    private int mImageResourceId;

    @ColumnInfo(name = "custom_marker_xcoord")
    private double markerXCoord;

    @ColumnInfo(name = "custom_marker_ycoord")
    private double markerYCoord;

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

    public int getImageResourceId() {
        return mImageResourceId;
    }

    public void setImageResourceId(int mImageResourceId) {
        this.mImageResourceId = mImageResourceId;
    }

    public double getMarkerXCoord() {
        return markerXCoord;
    }

    public void setMarkerXCoord(double markerXCoord) {
        this.markerXCoord = markerXCoord;
    }

    public double getMarkerYCoord() {
        return markerYCoord;
    }

    public void setMarkerYCoord(double markerYCoord) {
        this.markerYCoord = markerYCoord;
    }
}
