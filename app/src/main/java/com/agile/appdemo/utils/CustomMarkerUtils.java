package com.agile.appdemo.utils;

import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.agile.appdemo.database.entities.CustomMarker;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.agile.appdemo.ui.markeroverlaypage.AddMarkerDialog.TAG;

public class CustomMarkerUtils {

    public static int mMarkerRadius = 40;

//    public static void addCustomMarkerToFrameLayout(FrameLayout frameLayout, double xCoord, double yCoord, ImageView imageView) {
//
//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(mMarkerRadius, mMarkerRadius);
//        int startMargin = (int)xCoord - (mMarkerRadius / 2);
//        int topMargin = (int)yCoord - (mMarkerRadius / 2);
//
//        params.setMarginStart(startMargin);
//        params.topMargin = topMargin;
//
//        frameLayout.addView(imageView, params);
//    }

    public static void addCustomMarkerToFrameLayout(FrameLayout frameLayout, CustomMarker customMarker, ImageView markerImage) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(mMarkerRadius, mMarkerRadius);
        Log.d(TAG, "addCustomMarkerToFrameLayout: marker xcoord: " + customMarker.getMarkerXCoord());
        Log.d(TAG, "addCustomMarkerToFrameLayout: framelayout width: " + frameLayout.getWidth());
        int startMargin = (int)(frameLayout.getWidth() * customMarker.getMarkerXCoord());
        int topMargin = (int)(frameLayout.getHeight() * customMarker.getMarkerYCoord());
        Log.d(TAG, "addCustomMarkerToFrameLayout: startmargin: " + startMargin + " and topmargin: " + topMargin);
        params.setMarginStart(startMargin);
        params.topMargin = topMargin;
        Log.d(TAG, "addCustomMarkerToFrameLayout: adding imageview for marker with id: " + customMarker.getMarkerId());
        frameLayout.addView(markerImage, params);
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
