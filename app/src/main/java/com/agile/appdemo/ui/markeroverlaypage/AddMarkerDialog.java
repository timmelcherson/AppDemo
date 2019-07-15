package com.agile.appdemo.ui.markeroverlaypage;

import android.app.Dialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.agile.appdemo.R;
import com.agile.appdemo.database.entities.CustomMarker;
import com.agile.appdemo.utils.Constants;
import com.agile.appdemo.utils.CustomMarkerUtils;
import com.agile.appdemo.viewmodels.CustomMarkerViewModel;
import com.linroid.filtermenu.library.FilterMenu;
import com.linroid.filtermenu.library.FilterMenuLayout;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.agile.appdemo.animationutils.CustomAnimations.fadeInView;
import static com.agile.appdemo.utils.Constants.MARKER_INFO_DIALOG_TAG;
import static com.agile.appdemo.utils.Constants.OVERLAY_DIALOG_EXTRA_IMAGE_HEIGHT;
import static com.agile.appdemo.utils.Constants.OVERLAY_DIALOG_EXTRA_IMAGE_WIDTH;

public class AddMarkerDialog extends DialogFragment implements View.OnClickListener, FilterMenu.OnMenuChangeListener {

    public static final String TAG = "TAG";

    private int mImageWidth, mImageHeight, mScreenOrientation, xCoord, yCoord;
    private int mMarkerRadius = 40;
    private String mMarkerId;
    private FrameLayout mImageOverlay;
    private HashMap<String, double[]> mMarkerMap = new HashMap<>();
    private FilterMenuLayout mPickerMenuLayout;

    private MarkerInfoDialog mDialog;

    private CustomMarkerViewModel mCustomMarkerViewModel;
    private List<ImageView> mMarkerImageViews = new ArrayList<>();
    private List<CustomMarker> mMarkers = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.OverlayDialogStyle);

        Bundle b = getArguments();
        if (b != null) {
            if (b.containsKey(OVERLAY_DIALOG_EXTRA_IMAGE_WIDTH))
                mImageWidth = b.getInt(OVERLAY_DIALOG_EXTRA_IMAGE_WIDTH);
            if (b.containsKey(OVERLAY_DIALOG_EXTRA_IMAGE_HEIGHT))
                mImageHeight = b.getInt(OVERLAY_DIALOG_EXTRA_IMAGE_HEIGHT);
        }

        mCustomMarkerViewModel = ViewModelProviders.of(this).get(CustomMarkerViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view;

        mScreenOrientation = getResources().getConfiguration().orientation;

        if (mScreenOrientation == Configuration.ORIENTATION_PORTRAIT && getActivity() != null)
            view = inflater.inflate(R.layout.overlay_dialog_layout_portrait, container, false);
        else if (mScreenOrientation == Configuration.ORIENTATION_LANDSCAPE)
            view = inflater.inflate(R.layout.overlay_dialog_layout_landscape, container, false);
        else
            view = inflater.inflate(R.layout.overlay_dialog_layout_portrait, container, false);


        initializeViews(view);
        return view;
    }

    private void initializeViews(View view) {
        ImageView closeDialogButton = view.findViewById(R.id.overlay_dialog_close);
        Button saveButton = view.findViewById(R.id.overlay_save_button);
        /*mInitialMessage = view.findViewById(R.id.overlay_initial_message);
        Log.d(TAG, "initializeViews: start fade in");
        mInitialMessage.startAnimation(new AlphaAnimation(0.0f, 1.0f));
        Log.d(TAG, "initializeViews: fade in done, start delay");
        mInitialMessage.postDelayed(new Runnable() {
            public void run() {
                Log.d(TAG, "run: inside");
                mInitialMessage.setVisibility(View.GONE);
            }
        }, 1000);
        Log.d(TAG, "initializeViews: run over");*/

        mImageOverlay = view.findViewById(R.id.overlay_dialog_inner_container);
        ViewGroup.LayoutParams params = mImageOverlay.getLayoutParams();
        params.width = mImageWidth;
        params.height = mImageHeight;
        mImageOverlay.setLayoutParams(params);
        mPickerMenuLayout = view.findViewById(R.id.point_picker_menu);
        final FilterMenu pickerMenu = new FilterMenu.Builder(getActivity())
                .inflate(R.menu.point_picker_menu_items)
                .attach(mPickerMenuLayout)
                .withListener(this)
                .build();

        saveButton.setOnClickListener(this);
        closeDialogButton.setOnClickListener(this);

        mImageOverlay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    xCoord = (int) event.getX();
                    yCoord = (int) event.getY();
                    Log.d(TAG, "onTouch: xcoord with no int cast: " + event.getX());
                    fadeInView(mPickerMenuLayout);
                    pickerMenu.expand(true);
                    mPickerMenuLayout.bringToFront();
                }
                return false;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams windowParams = dialog.getWindow().getAttributes();
            windowParams.dimAmount = 0.0f;
            dialog.getWindow().setAttributes(windowParams);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.overlay_dialog_close:
                dismiss();
                break;

            case R.id.overlay_dialog_inner_container:
                break;

            case R.id.overlay_save_button:
                onSaveMarkers();
                dismiss();
                break;
        }
    }

    @Override
    public void onMenuItemClick(View view, int position) {

        ImageView markerImage = new ImageView(getActivity());
        int drawableId = 0;

        switch (position) {
            case 0:
                drawableId = R.drawable.image_marker_red_blank;
                markerImage.setImageResource(drawableId);
                break;
            case 1:
                drawableId = R.drawable.image_marker_red_cross;
                markerImage.setImageResource(drawableId);
                break;
            case 2:
                drawableId = R.drawable.image_marker_blue_blank;
                markerImage.setImageResource(drawableId);
                break;
            case 3:
                drawableId = R.drawable.image_marker_blue_cross;
                markerImage.setImageResource(drawableId);
                break;
            case 4:
                drawableId = R.drawable.image_marker_green_blank;
                markerImage.setImageResource(drawableId);
                break;
            case 5:
                drawableId = R.drawable.image_marker_green_cross;
                markerImage.setImageResource(drawableId);
                break;
        }

        mMarkerImageViews.add(markerImage);

        Log.d(TAG, "CHECK FOR ID, markerImage.getId(): " + markerImage.getId() + " AND ACTUAL DRAWABLE ID: " + R.drawable.image_marker_blue_cross);

//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(mMarkerRadius, mMarkerRadius);
//        int startMargin = xCoord - (mMarkerRadius / 2);
//        int topMargin = yCoord - (mMarkerRadius / 2);
//
//        params.setMarginStart(startMargin);
//        params.topMargin = topMargin;
//
//        mImageOverlay.addView(markerImage, params);


//        double[] distanceFractions = new double[2];
//        distanceFractions[0] = xCoord - (int)(CustomMarkerUtils.mMarkerRadius / 2);
//        distanceFractions[1] = yCoord - (int)(CustomMarkerUtils.mMarkerRadius / 2);
//        distanceFractions[0] = round(xCoord - (int)(CustomMarkerUtils.mMarkerRadius / 2) / (double) mImageWidth, 2);
//        distanceFractions[1] = round(yCoord - (int)(CustomMarkerUtils.mMarkerRadius / 2) / (double) mImageHeight, 2);
        mMarkerId = UUID.randomUUID().toString();

        CustomMarker customMarker = new CustomMarker();
        customMarker.setMarkerId(mMarkerId);
        customMarker.setPlanId(MarkerOverlayActivity.mPlanId);
        customMarker.setMarkerXCoord(round((xCoord - (mMarkerRadius / 2)) / (double) mImageWidth, 4));
        customMarker.setMarkerYCoord(round((yCoord - (mMarkerRadius / 2)) / (double) mImageHeight, 4));
        customMarker.setImageResourceId(drawableId);
        Log.d(TAG, "onMenuItemClick: MARKER STARTMARGIN: " + (xCoord - (mMarkerRadius / 2)));
        Log.d(TAG, "Saving new marker with xcoord: " + customMarker.getMarkerXCoord() + " and planid: " + customMarker.getPlanId());
        mMarkers.add(customMarker);

//        CustomMarkerUtils.addCustomMarkerToFrameLayout(mImageOverlay, xCoord, yCoord, markerImage);
        CustomMarkerUtils.addCustomMarkerToFrameLayout(mImageOverlay, customMarker, markerImage);

        mDialog = new MarkerInfoDialog();
        Bundle b = new Bundle();
        b.putString(Constants.MARKER_INFO_DIALOG_MARKER_ID, mMarkerId);
        mDialog.setArguments(b);
        FragmentTransaction fragmentTransactionOne = getActivity().getSupportFragmentManager().beginTransaction();
        mDialog.show(fragmentTransactionOne, MARKER_INFO_DIALOG_TAG);

//        mMarkerMap.put(mMarkerId, distanceFractions);

        markerImage.setOnClickListener((View v) -> {
            MarkerInfoDialog dialog = new MarkerInfoDialog();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.MARKER_INFO_DIALOG_MARKER_ID, mMarkerId);
            dialog.setArguments(bundle);
            FragmentTransaction fragmentTransactionTwo = getActivity().getSupportFragmentManager().beginTransaction();
            dialog.show(fragmentTransactionTwo, MARKER_INFO_DIALOG_TAG);
        });
    }

    private void addCustomMarkerToFrameLayout(FrameLayout frameLayout, CustomMarker customMarker, ImageView markerImage) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(mMarkerRadius, mMarkerRadius);
        Log.d(TAG, "addCustomMarkerToFrameLayout: marker xcoord: " + customMarker.getMarkerXCoord());
        Log.d(TAG, "addCustomMarkerToFrameLayout: framelayout width: " + frameLayout.getWidth());
        int startMargin = (int) (frameLayout.getWidth() * customMarker.getMarkerXCoord());
        int topMargin = (int) (frameLayout.getHeight() * customMarker.getMarkerYCoord());
        Log.d(TAG, "addCustomMarkerToFrameLayout: startmargin: " + startMargin + " and topmargin: " + topMargin);
        params.setMarginStart(startMargin);
        params.topMargin = topMargin;
        Log.d(TAG, "addCustomMarkerToFrameLayout: adding imageview for marker with id: " + customMarker.getMarkerId());
        frameLayout.addView(markerImage, params);
    }

    private void onSaveMarkers() {

//        for (String key : mMarkerMap.keySet()) {
//            CustomMarker customMarker = new CustomMarker();
//            customMarker.setMarkerId(key);
//            customMarker.setMarkerXCoord(mMarkerMap.get(key)[0]);
//            customMarker.setMarkerYCoord(mMarkerMap.get(key)[1]);
//
//            Log.d(TAG, "gonna save marker: " + customMarker + " with id: " + customMarker.getMarkerId()
//                    + " and distanceFractions: (" + customMarker.getMarkerXCoord() + ", " + customMarker.getMarkerYCoord() + ")");
//        }

        Log.d(TAG, "onSaveMarkers: init");
        for (CustomMarker marker : mMarkers) {
            mCustomMarkerViewModel.insert(marker);
            Log.d(TAG, "onSaveMarkers: saving marker with id: " + marker.getMarkerId() + " to database");
        }
        Log.d(TAG, "onSaveMarkers: clearing");
        mMarkers.clear();
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    public void onMenuCollapse() {
        mPickerMenuLayout.setVisibility(View.GONE);
    }

    @Override
    public void onMenuExpand() {

    }
}
