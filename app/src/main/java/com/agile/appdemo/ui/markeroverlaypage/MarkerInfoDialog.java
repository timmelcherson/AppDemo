package com.agile.appdemo.ui.markeroverlaypage;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.agile.appdemo.R;
import com.agile.appdemo.utils.Constants;

public class MarkerInfoDialog extends DialogFragment implements View.OnClickListener {

    private static final String TAG = "TAG";

    /* Views */
    private TextView mSampleNumberTv;
    private ImageView mCloseMarkerInfoDialog;

    /* Form items */
    private Spinner mMaterialTypeSpinner, mCriteriaSpinner;
    private RadioGroup mSurveyOrObservationGroup, mPresenceOfAsbestosGroup;
    private Switch mSampleTakenSwitch;

    /* Data */
    private int mSampleNumberDigits;

    /* Display */
    private String markerUUID, mMaterialType, mCriteriaForDecision, mSurveyOrObservation, mPresenceOfAsbestos, mSampleId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.OverlayDialogStyle);

        Bundle b = getArguments();
        if (b != null) {
            if (b.containsKey(Constants.MARKER_INFO_DIALOG_MARKER_ID))
                markerUUID = b.getString(Constants.MARKER_INFO_DIALOG_MARKER_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view;

        if (getDialog() != null && getDialog().getWindow() != null) {
            Log.d(TAG, "onCreateView: setting color");

        }

        int screenOrientation = getResources().getConfiguration().orientation;

        if (screenOrientation == Configuration.ORIENTATION_PORTRAIT && getActivity() != null)
            view = inflater.inflate(R.layout.marker_info_dialog_layout_portrait, container, false);
        else if (screenOrientation == Configuration.ORIENTATION_LANDSCAPE)
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

        /* -------------- Entry One -------------- */
        mMaterialTypeSpinner = view.findViewById(R.id.marker_info_material_type_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.marker_info_material_types, R.layout.spinner_main_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMaterialTypeSpinner.setAdapter(adapter);

        /* -------------- Entry Two -------------- */
        mCriteriaSpinner = view.findViewById(R.id.marker_info_criteria_for_decision_spinner);
        adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.decision_criterias, R.layout.spinner_main_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCriteriaSpinner.setAdapter(adapter);

        /* -------------- Entry Three -------------- */
        mSurveyOrObservationGroup = view.findViewById(R.id.survey_observation_group);

        /* -------------- Entry Four -------------- */
        mPresenceOfAsbestosGroup = view.findViewById(R.id.presence_of_asbestos_group);

        /* -------------- Entry Five -------------- */
        mSampleNumberTv = view.findViewById(R.id.marker_info_sample_number);
        mSampleNumberTv.setText("S0073");
        String str = (String) mSampleNumberTv.getText();
        mSampleNumberDigits = Integer.parseInt(str.replaceAll("[^\\d.]", ""));
        mSampleTakenSwitch = view.findViewById(R.id.marker_info_sample_taken_switch);

        /* Set listeners to views*/
        setListeners();
    }

    private void setListeners() {

        mCloseMarkerInfoDialog.setOnClickListener(this);

        /* -------------- Entry One -------------- */
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

        /* -------------- Entry Two -------------- */
        mCriteriaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCriteriaForDecision = parent.getItemAtPosition(position).toString();
                Log.d(TAG, "onItemSelected: " + mCriteriaForDecision);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "onNothingSelected: WTF");
            }
        });

        /* -------------- Entry Three -------------- */
        mSurveyOrObservationGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = group.findViewById(checkedId);
                if (rb != null)
                    mSurveyOrObservation = (String)rb.getText();
                Toast.makeText(getActivity(), "Checked: " + mSurveyOrObservation, Toast.LENGTH_SHORT).show();
            }
        });

        /* -------------- Entry Four -------------- */
        mPresenceOfAsbestosGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = group.findViewById(checkedId);
                if (rb != null)
                    mPresenceOfAsbestos = (String)rb.getText();
                Toast.makeText(getActivity(), "Checked: " + mPresenceOfAsbestos, Toast.LENGTH_SHORT).show();
            }
        });

        /* -------------- Entry Five -------------- */
        mSampleTakenSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked)
                    mSampleNumberDigits++;
                else {
                    if (mSampleNumberDigits > 0)
                        mSampleNumberDigits--;
                }
                mSampleId = String.format("S%04d", mSampleNumberDigits);
                mSampleNumberTv.setText(mSampleId);
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
