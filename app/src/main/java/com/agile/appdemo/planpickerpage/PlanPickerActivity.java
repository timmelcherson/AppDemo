package com.agile.appdemo.planpickerpage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.agile.appdemo.database.entities.Plan;
import com.agile.appdemo.planpickerpage.markeroverlaypage.MarkerOverlayActivity;
import com.agile.appdemo.R;
import com.agile.appdemo.utils.Constants;
import com.agile.appdemo.viewmodels.PlanViewModel;

import java.util.ArrayList;
import java.util.List;

public class PlanPickerActivity extends AppCompatActivity implements PlanPickerRecyclerAdapter.OnImageItemsItemListener, View.OnClickListener {

    public static final String TAG = "TAG";

    private View rootLayout;
    private RecyclerView mRecyclerView;
    private Button mBackButton;

    private List<PlanPickerCard> mItemList = new ArrayList<>();
    private PlanViewModel mPlanViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_picker);

        rootLayout = findViewById(R.id.image_picker_layout);
        mRecyclerView = findViewById(R.id.image_item_recycler_view);
        mBackButton = findViewById(R.id.item_picker_back_btn);
        mBackButton.setOnClickListener(this);

        mItemList.add(new PlanPickerCard(R.drawable.blueprint_1, getString(R.string.picker_item_title_1)));
        mItemList.add(new PlanPickerCard(R.drawable.house, getString(R.string.picker_item_title_2)));
        mItemList.add(new PlanPickerCard(R.drawable.emergency, getString(R.string.picker_item_title_3)));

        mPlanViewModel = ViewModelProviders.of(this).get(PlanViewModel.class);
        mPlanViewModel.observablePlanList().observe(this, (List<Plan> plans) -> {
            for (Plan plan : plans)
                Log.d(TAG, "Observed plan with name: " + plan.getPlanName());
        });

        buildRecyclerView();
    }


    private void buildRecyclerView() {
        LinearLayoutManager lm = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(lm);
        PlanPickerRecyclerAdapter adapter = new PlanPickerRecyclerAdapter(mItemList, this);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.item_picker_back_btn:
                Log.d(TAG, "onClick: ");
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(int position) {

        Intent intent = new Intent(this, MarkerOverlayActivity.class);
        intent.putExtra(Constants.ADD_OVERLAY_INTENT_KEY, mItemList.get(position).getImageResource());
        Log.d(TAG, "sending int: " + mItemList.get(position).getImageResource());
        startActivity(intent);

//        switch (position) {
//
//            case 0:
//                intent.putExtra(Constants.ADD_OVERLAY_INTENT_BLUEPRINT, mItemList.get(position).getImageResource());
//                break;
//            case 1:
//                intent.putExtra(Constants.ADD_OVERLAY_INTENT_HAND_DRAWING, mItemList.get(position).getImageResource());
//                break;
//            case 2:
//                intent.putExtra(Constants.ADD_OVERLAY_INTENT_EMERGENCY, mItemList.get(position).getImageResource());
//                break;
//        }
    }

}
