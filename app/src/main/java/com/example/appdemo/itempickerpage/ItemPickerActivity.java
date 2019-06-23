package com.example.appdemo.itempickerpage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.example.appdemo.itempickerpage.markeroverlaypage.MarkerOverlayActivity;
import com.example.appdemo.R;
import com.example.appdemo.animationutils.CustomAnimations;
import com.example.appdemo.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import static com.example.appdemo.utils.Constants.EXTRA_CIRCULAR_REVEAL_X;
import static com.example.appdemo.utils.Constants.EXTRA_CIRCULAR_REVEAL_Y;

public class ItemPickerActivity extends AppCompatActivity implements ItemPickerRecyclerAdapter.OnImageItemsItemListener{

    public static final String TAG = "TAG";

    private CustomAnimations mCustomAnimations;
    private View rootLayout;
    private RecyclerView mRecyclerView;

    private List<ItemPickerCard> mItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_picker);


        rootLayout = findViewById(R.id.image_picker_layout);
        mRecyclerView = findViewById(R.id.image_item_recycler_view);

        mCustomAnimations = new CustomAnimations();

        mItemList.add(new ItemPickerCard(R.drawable.blueprint_1, getString(R.string.image_item_blueprint_title)));
        mItemList.add(new ItemPickerCard(R.drawable.house, getString(R.string.image_item_hand_drawn_title)));
        mItemList.add(new ItemPickerCard(R.drawable.emergency, getString(R.string.image_item_emergency_title)));
        Log.d(TAG, "onCreate: list is this size: " + mItemList.size());

        if (savedInstanceState == null) {
//            getIncomingIntent();
        }
        buildRecyclerView();

    }

    private void getIncomingIntent() {

        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_CIRCULAR_REVEAL_X) && intent.hasExtra(EXTRA_CIRCULAR_REVEAL_Y)) {
            rootLayout.setVisibility(View.INVISIBLE);

            final int revealX = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_X, 0);
            final int revealY = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_Y, 0);

            ViewTreeObserver viewTreeObserver = rootLayout.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mCustomAnimations.revealActivity(revealX, revealY, rootLayout);
                        rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }
        }
    }

    private void buildRecyclerView() {
        LinearLayoutManager lm = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(lm);
        ItemPickerRecyclerAdapter adapter = new ItemPickerRecyclerAdapter(mItemList, this);
        mRecyclerView.setAdapter(adapter);
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
