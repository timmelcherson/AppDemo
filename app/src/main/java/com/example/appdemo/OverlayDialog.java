package com.example.appdemo;

import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import static com.example.appdemo.utils.Constants.OVERLAY_DIALOG_EXTRA_IMAGE_HEIGHT;
import static com.example.appdemo.utils.Constants.OVERLAY_DIALOG_EXTRA_IMAGE_WIDTH;

public class OverlayDialog extends DialogFragment implements View.OnClickListener{

    public static final String TAG = "TAG";

    private int mImageWidth, mImageHeight, mScreenOrientation;
    private FrameLayout mImageOverlay;
    private ImageView mCloseDialogButton;


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
            view = view = inflater.inflate(R.layout.overlay_dialog_layout_portrait, container, false);


        mCloseDialogButton = view.findViewById(R.id.overlay_dialog_close);
        mCloseDialogButton.setOnClickListener(this);

        mImageOverlay = view.findViewById(R.id.overlay_dialog_inner_container);
        ViewGroup.LayoutParams params = mImageOverlay.getLayoutParams();
        params.width = mImageWidth;
        params.height = mImageHeight;
        mImageOverlay.setLayoutParams(params);

        Log.d(TAG, "onCreateView: inner overlay width: " + mImageOverlay.getWidth() + " and measured ?? width: " + mImageOverlay.getMeasuredWidth());
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
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
        }
    }
}
