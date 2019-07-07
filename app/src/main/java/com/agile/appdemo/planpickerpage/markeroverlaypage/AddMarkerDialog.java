package com.agile.appdemo.planpickerpage.markeroverlaypage;

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

import com.agile.appdemo.R;
import com.agile.appdemo.utils.Constants;
import com.linroid.filtermenu.library.FilterMenu;
import com.linroid.filtermenu.library.FilterMenuLayout;

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
    private UUID uuid;
    private String mMarkerId;
    private ImageView marker;
    private FrameLayout mImageOverlay;
    private ImageView mCloseDialogButton;
    private TextView mInitialMessage;
    private Button mSaveButton;
    private HashMap<UUID, List<Integer>> mMarkerCoordinatesMap = new HashMap<>();
    private List<Integer> mMarkerCoordinates;
    private FilterMenuLayout mPickerMenuLayout;

    private FragmentTransaction ft;
    private MarkerInfoDialog dialog;

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
        mCloseDialogButton = view.findViewById(R.id.overlay_dialog_close);
        mSaveButton = view.findViewById(R.id.overlay_save_button);
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

        mSaveButton.setOnClickListener(this);
        mCloseDialogButton.setOnClickListener(this);

        mImageOverlay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    xCoord = (int) event.getX();
                    yCoord = (int) event.getY();
                    Log.d(TAG, "onTouch: xCoord: " + xCoord);
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
                Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
                dismiss();
                break;
        }
    }

    @Override
    public void onMenuItemClick(View view, int position) {

        marker = new ImageView(getActivity());

        switch (position) {
            case 0:
                marker.setImageResource(R.drawable.image_marker_red_blank);
                break;
            case 1:
                marker.setImageResource(R.drawable.image_marker_red_cross);
                break;
            case 2:
                marker.setImageResource(R.drawable.image_marker_blue_blank);
                break;
            case 3:
                marker.setImageResource(R.drawable.image_marker_blue_cross);
                break;
            case 4:
                marker.setImageResource(R.drawable.image_marker_green_blank);
                break;
            case 5:
                marker.setImageResource(R.drawable.image_marker_green_cross);
                break;
        }

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(mMarkerRadius, mMarkerRadius);
        int startMargin = xCoord - (mMarkerRadius / 2);
        int topMargin = yCoord - (mMarkerRadius / 2);
        mMarkerCoordinates = new ArrayList<>();
        mMarkerCoordinates.add(startMargin);
        mMarkerCoordinates.add(topMargin);
        params.setMarginStart(startMargin);
        params.topMargin = topMargin;

        mImageOverlay.addView(marker, params);

        mMarkerId = UUID.randomUUID().toString();
        marker.setTag(mMarkerId);

        int[] coordinates = new int[2];
        coordinates[0] = startMargin;
        coordinates[1] = topMargin;

        dialog = new MarkerInfoDialog();
        Bundle b = new Bundle();
        b.putString(Constants.MARKER_INFO_DIALOG_MARKER_ID, mMarkerId);
        dialog.setArguments(b);
        ft = getActivity().getSupportFragmentManager().beginTransaction();
        dialog.show(ft, MARKER_INFO_DIALOG_TAG);

//        mMarkerCoordinatesMap.put(uuid, mMarkerCoordinates);

        marker.setOnClickListener((View v) -> {
            MarkerInfoDialog dialog = new MarkerInfoDialog();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.MARKER_INFO_DIALOG_MARKER_ID, mMarkerId);
            dialog.setArguments(bundle);
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            dialog.show(ft, MARKER_INFO_DIALOG_TAG);
        });
    }


    @Override
    public void onMenuCollapse() {
        mPickerMenuLayout.setVisibility(View.GONE);
    }

    @Override
    public void onMenuExpand() {

    }
}
