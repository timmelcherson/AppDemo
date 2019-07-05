package com.agile.appdemo.planpickerpage.markeroverlaypage;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.agile.appdemo.BaseActivity;
import com.agile.appdemo.R;
import com.agile.appdemo.database.entities.CustomMarker;
import com.agile.appdemo.viewmodels.CustomMarkerViewModel;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

import java.util.List;

import static com.agile.appdemo.utils.Constants.ADD_OVERLAY_INTENT_KEY;
import static com.agile.appdemo.utils.Constants.OVERLAY_DIALOG_EXTRA_IMAGE_HEIGHT;
import static com.agile.appdemo.utils.Constants.OVERLAY_DIALOG_EXTRA_IMAGE_WIDTH;
import static com.agile.appdemo.utils.Constants.OVERLAY_DIALOG_TAG;

public class MarkerOverlayActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = "TAG";

    private int mDrawableId, mOverlayImageWidth, mOverlayImageHeight, mScreenOrientation;
    //    private ImageView mOverlayImage;
    private TextView mOverlayActionAddPoint;
    private PhotoViewAttacher mAttacher;
    private PhotoView mOverlayImage;
    private Button mBackButton;
    private CustomMarkerViewModel mCustomMarkerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mScreenOrientation = getResources().getConfiguration().orientation;
        if (mScreenOrientation == Configuration.ORIENTATION_PORTRAIT)
            setContentView(R.layout.activity_marker_overlay_portrait);

        if (mScreenOrientation == Configuration.ORIENTATION_LANDSCAPE)
            setContentView(R.layout.activity_marker_overlay_landscape);

        mOverlayImage = findViewById(R.id.overlay_image);
        mOverlayActionAddPoint = findViewById(R.id.overlay_action_add_point);
        mBackButton = findViewById(R.id.add_overlay_back_btn);

        mBackButton.setOnClickListener(this);
        mOverlayActionAddPoint.setOnClickListener(this);

        mCustomMarkerViewModel = ViewModelProviders.of(this).get(CustomMarkerViewModel.class);
        mCustomMarkerViewModel.observableCustomMarkerList().observe(this, (List<CustomMarker> customMarkers) -> {
            if (customMarkers != null && customMarkers.size() > 0) {
                for (CustomMarker marker : customMarkers)
                    Log.d(TAG, "Observed marker with id: " + marker.getMarkerId());
            }
        });
        getIncomingIntent();
    }

    private void getIncomingIntent() {

        if (getIntent().hasExtra(ADD_OVERLAY_INTENT_KEY)) {
            mDrawableId = getIntent().getIntExtra(ADD_OVERLAY_INTENT_KEY, 0);
            mOverlayImage.setImageResource(mDrawableId);
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
