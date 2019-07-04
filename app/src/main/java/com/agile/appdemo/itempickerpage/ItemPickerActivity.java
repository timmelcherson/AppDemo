package com.agile.appdemo.itempickerpage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.agile.appdemo.itempickerpage.markeroverlaypage.MarkerOverlayActivity;
import com.agile.appdemo.R;
import com.agile.appdemo.animationutils.CustomAnimations;
import com.agile.appdemo.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import static com.agile.appdemo.utils.Constants.EXTRA_CIRCULAR_REVEAL_X;
import static com.agile.appdemo.utils.Constants.EXTRA_CIRCULAR_REVEAL_Y;

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

        mItemList.add(new ItemPickerCard(R.drawable.blueprint_1, getString(R.string.picker_item_title_1)));
        mItemList.add(new ItemPickerCard(R.drawable.house, getString(R.string.picker_item_title_2)));
        mItemList.add(new ItemPickerCard(R.drawable.emergency, getString(R.string.picker_item_title_3)));

        buildRecyclerView();
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
