package com.example.appdemo;

import android.app.Dialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import static com.example.appdemo.utils.Constants.MARKER_INFO_DIALOG_EXTRA_UUID;
import static com.example.appdemo.utils.Constants.OVERLAY_DIALOG_EXTRA_IMAGE_HEIGHT;
import static com.example.appdemo.utils.Constants.OVERLAY_DIALOG_EXTRA_IMAGE_WIDTH;

public class MarkerInfoDialog extends DialogFragment implements View.OnClickListener {

    public static final String TAG = "TAG";

    private int mScreenOrientation;
    private String markerUUID, mMaterialType;
    private Spinner mMaterialTypeSpinner;
    private ImageView mCloseMarkerInfoDialog;
    private Switch mSampleTakenSwitch;
    private TextView mSampleNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.OverlayDialogStyle);

        Bundle b = getArguments();
        if (b != null) {
            if (b.containsKey(MARKER_INFO_DIALOG_EXTRA_UUID))
                markerUUID = b.getString(MARKER_INFO_DIALOG_EXTRA_UUID);
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
            view = inflater.inflate(R.layout.marker_info_dialog_layout_portrait, container, false);
        else if (mScreenOrientation == Configuration.ORIENTATION_LANDSCAPE)
            view = inflater.inflate(R.layout.marker_info_dialog_layout_landscape, container, false);
        else
            view = inflater.inflate(R.layout.marker_info_dialog_layout_portrait, container, false);


        initializeViews(view);
        return view;
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

    private void initializeViews(View view) {

        mCloseMarkerInfoDialog = view.findViewById(R.id.close_marker_info_dialog);
        TextView test = view.findViewById(R.id.marker_uuid);
        test.setText(markerUUID);

        mSampleNumber = view.findViewById(R.id.marker_info_sample_number);
        mSampleTakenSwitch = view.findViewById(R.id.marker_info_sample_taken_switch);
        mSampleTakenSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int num = 0;

                if (isChecked)
                    num = Integer.parseInt(mSampleNumber.getText().toString()) + 1;
                else {
                    if (Integer.parseInt(mSampleNumber.getText().toString()) > 0)
                        num = Integer.parseInt(mSampleNumber.getText().toString()) - 1;
                }


                mSampleNumber.setText(String.valueOf(num));
            }
        });

        mMaterialTypeSpinner = view.findViewById(R.id.marker_info_material_type_spinner);
        String st[] = getResources().getStringArray(R.array.marker_info_material_types);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.marker_info_material_types, R.layout.spinner_main_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMaterialTypeSpinner.setAdapter(adapter);
        setListeners();
    }

    private void setListeners() {

        mCloseMarkerInfoDialog.setOnClickListener(this);
        mMaterialTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mMaterialType = parent.getItemAtPosition(position).toString();
                Log.d(TAG, "onItemSelected: " + mMaterialType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "onNothingSelected: WTF");
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.close_marker_info_dialog:
                dismiss();
                break;
        }
    }
}
