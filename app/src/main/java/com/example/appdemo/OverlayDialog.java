package com.example.appdemo;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.appdemo.animationutils.CustomAnimations;
import com.linroid.filtermenu.library.FilterMenu;
import com.linroid.filtermenu.library.FilterMenuLayout;

import org.w3c.dom.Text;

import java.util.UUID;

import static com.example.appdemo.animationutils.CustomAnimations.fadeInView;
import static com.example.appdemo.animationutils.CustomAnimations.fadeOutView;
import static com.example.appdemo.utils.Constants.MARKER_INFO_DIALOG_EXTRA_UUID;
import static com.example.appdemo.utils.Constants.MARKER_INFO_DIALOG_TAG;
import static com.example.appdemo.utils.Constants.OVERLAY_DIALOG_EXTRA_IMAGE_HEIGHT;
import static com.example.appdemo.utils.Constants.OVERLAY_DIALOG_EXTRA_IMAGE_WIDTH;

public class OverlayDialog extends DialogFragment implements View.OnClickListener {

    public static final String TAG = "TAG";

    private int mImageWidth, mImageHeight, mScreenOrientation, xCoord, yCoord;
    private int mMarkerRadius = 60;

    private FrameLayout mImageOverlay;
    private ImageView mCloseDialogButton;
    private TextView mInitialMessage;
    private Button mSaveButton;


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

        if (getDialog() != null && getDialog().getWindow() != null) {
            Log.d(TAG, "onCreateView: setting color");

        }

        mScreenOrientation = getResources().getConfiguration().orientation;

        if (mScreenOrientation == Configuration.ORIENTATION_PORTRAIT && getActivity() != null)
            view = inflater.inflate(R.layout.overlay_dialog_layout_portrait, container, false);
        else if (mScreenOrientation == Configuration.ORIENTATION_LANDSCAPE)
            view = inflater.inflate(R.layout.overlay_dialog_layout_landscape, container, false);
        else
            view = view = inflater.inflate(R.layout.overlay_dialog_layout_portrait, container, false);


        initializeViews(view);
        Log.d(TAG, "onCreateView: inner overlay width: " + mImageOverlay.getWidth() + " and measured ?? width: " + mImageOverlay.getMeasuredWidth());
        return view;
    }
    private UUID uuid;
    private ImageView marker;

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
        final FilterMenuLayout pickerMenuLayout = (FilterMenuLayout) view.findViewById(R.id.point_picker_menu);
        final FilterMenu pickerMenu = new FilterMenu.Builder(getActivity())
                .inflate(R.menu.point_picker_menu_items)//inflate  menu resource
                .attach(pickerMenuLayout)
                .withListener(new FilterMenu.OnMenuChangeListener() {
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

                        Log.d(TAG, "onMenuItemClick: xCoord: " + xCoord + " y-coord: " + yCoord);

                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(mMarkerRadius, mMarkerRadius);
                        params.leftMargin = xCoord - (mMarkerRadius / 2);
                        params.topMargin = yCoord - (mMarkerRadius / 2);
                        mImageOverlay.addView(marker, params);
                        uuid = UUID.randomUUID();
                        marker.setTag(uuid);
                        marker.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MarkerInfoDialog dialog = new MarkerInfoDialog();
                                Bundle b = new Bundle();
                                b.putString(MARKER_INFO_DIALOG_EXTRA_UUID, marker.getTag().toString());
                                dialog.setArguments(b);
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                dialog.show(ft, MARKER_INFO_DIALOG_TAG);
                            }
                        });
                        Log.d(TAG, "image UUID: " + marker.getTag());
                        Log.d(TAG, "image width: " + marker.getWidth() + " marker height: " + marker.getHeight());
                        Log.d(TAG, "marker left: " + marker.getLeft() + " marker top: " + marker.getTop());
                    }

                    @Override
                    public void onMenuCollapse() {
                        pickerMenuLayout.setVisibility(View.GONE);
                        Log.d(TAG, "onMenuCollapse: ");
                    }

                    @Override
                    public void onMenuExpand() {
                        Log.d(TAG, "onMenuExpand: ");
                    }
                })
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
                    fadeInView(pickerMenuLayout);
                    pickerMenu.expand(true);

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
            Log.d(TAG, "onStart: width: " + width + ", hieght: " + height);
            Log.d(TAG, "onStart: mImageWidth: " + mImageWidth + ", mImageHeight: " + mImageHeight);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.overlay_dialog_close:
                dismiss();
                break;

            case R.id.overlay_dialog_inner_container:
                Log.d(TAG, "onClick: it was also clicked");
                break;

            case R.id.overlay_save_button:
                Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
                dismiss();
                break;
        }
    }
}
