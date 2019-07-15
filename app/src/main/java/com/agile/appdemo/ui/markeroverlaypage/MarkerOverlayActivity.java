package com.agile.appdemo.ui.markeroverlaypage;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.agile.appdemo.BaseActivity;
import com.agile.appdemo.R;
import com.agile.appdemo.database.entities.CustomMarker;
import com.agile.appdemo.database.entities.Plan;
import com.agile.appdemo.utils.Constants;
import com.agile.appdemo.utils.CustomMarkerUtils;
import com.agile.appdemo.viewmodels.CustomMarkerViewModel;
import com.agile.appdemo.viewmodels.PlanViewModel;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

import java.util.ArrayList;
import java.util.List;

import static com.agile.appdemo.utils.Constants.OVERLAY_DIALOG_EXTRA_IMAGE_HEIGHT;
import static com.agile.appdemo.utils.Constants.OVERLAY_DIALOG_EXTRA_IMAGE_WIDTH;
import static com.agile.appdemo.utils.Constants.OVERLAY_DIALOG_TAG;

public class MarkerOverlayActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = "TAG";

    private int  mOverlayImageWidth, mOverlayImageHeight, mScreenOrientation;
    public static int mPlanId;
    //    private ImageView mOverlayImage;
    private TextView mOverlayActionAddPoint, mOverlayActionRemoveMarkers;
    private PhotoViewAttacher mAttacher;
    private PhotoView mOverlayImage;
    private FrameLayout mOverlayImageContainer;
    private Button mBackButton;
    private CustomMarkerViewModel mCustomMarkerViewModel;
    private PlanViewModel mPlanViewModel;
    private Plan mSelectedPlan;
    private List<CustomMarker> mCustomMarkers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        mScreenOrientation = getResources().getConfiguration().orientation;
        if (mScreenOrientation == Configuration.ORIENTATION_PORTRAIT)
            setContentView(R.layout.activity_marker_overlay_portrait);

        if (mScreenOrientation == Configuration.ORIENTATION_LANDSCAPE)
            setContentView(R.layout.activity_marker_overlay_landscape);

        mOverlayImage = findViewById(R.id.overlay_image);
        mOverlayImageContainer = findViewById(R.id.overlay_image_container);
        mOverlayActionAddPoint = findViewById(R.id.overlay_action_add_point);
        mOverlayActionRemoveMarkers = findViewById(R.id.overlay_action_remove_markers);
        mBackButton = findViewById(R.id.add_overlay_back_btn);

        mBackButton.setOnClickListener(this);
        mOverlayActionAddPoint.setOnClickListener(this);
        mOverlayActionRemoveMarkers.setOnClickListener(this);

        mCustomMarkerViewModel = ViewModelProviders.of(this).get(CustomMarkerViewModel.class);
        mPlanViewModel = ViewModelProviders.of(this).get(PlanViewModel.class);

        mCustomMarkerViewModel.observableCustomMarkerList().observe(this, (List<CustomMarker> customMarkers) -> {
            if (customMarkers != null && customMarkers.size() > 0) {
                for (CustomMarker marker : customMarkers) {
                    Log.d(TAG, "Observed marker with plan id: " + marker.getPlanId());

                    if (marker.getPlanId() == mPlanId){
                        mCustomMarkers.add(marker);

                        Log.d(TAG, "onCreate: 1");
                        ImageView iv = new ImageView(this);
                        Log.d(TAG, "onCreate: 2");
                        iv.setImageDrawable(getDrawable(marker.getImageResourceId()));
                        Log.d(TAG, "onCreate: 3");
                        CustomMarkerUtils.addCustomMarkerToFrameLayout(mOverlayImageContainer, marker, iv);
                    }
                }
            }
        });
        getIncomingIntent();
    }

    private void getIncomingIntent() {

        Intent intent = getIntent();

        if (intent.hasExtra(Constants.MARKER_OVERLAY_INTENT_PLAN_ID)) {
            mPlanId = intent.getIntExtra(Constants.MARKER_OVERLAY_INTENT_PLAN_ID, 0);
            Log.d(TAG, "getIncomingIntent: receiving plan with id: " + mPlanId);
            mSelectedPlan = mPlanViewModel.getPlan(mPlanId);
//            mOverlayImage.setImageResource(mDrawableId);
        }

        if (intent.hasExtra(Constants.MARKER_OVERLAY_INTENT_IMAGE_RES)) {
            mOverlayImage.setImageDrawable(getDrawable(intent.getIntExtra(Constants.MARKER_OVERLAY_INTENT_IMAGE_RES, 0)));
        }

        detectScreenDimens();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.overlay_action_add_point:
                AddMarkerDialog dialog = new AddMarkerDialog();
                Bundle b = new Bundle();
                b.putInt(OVERLAY_DIALOG_EXTRA_IMAGE_WIDTH, mOverlayImageWidth);
                b.putInt(OVERLAY_DIALOG_EXTRA_IMAGE_HEIGHT, mOverlayImageHeight);
                dialog.setArguments(b);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                dialog.show(ft, OVERLAY_DIALOG_TAG);
                break;

            case R.id.add_overlay_back_btn:
                finish();
                break;

            case R.id.overlay_action_remove_markers:

                for (CustomMarker marker : mCustomMarkers)
                    mCustomMarkerViewModel.delete(marker);

                recreate();
                break;
        }
    }


    private void detectScreenDimens() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        ViewTreeObserver viewTreeObserver = mOverlayImage.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mOverlayImage.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    mOverlayImageWidth = mOverlayImage.getWidth();
                    mOverlayImageHeight = mOverlayImage.getHeight();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
