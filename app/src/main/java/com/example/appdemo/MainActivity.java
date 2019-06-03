package com.example.appdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.appdemo.animationutils.CustomAnimations;
import com.linroid.filtermenu.library.FilterMenu;
import com.linroid.filtermenu.library.FilterMenuLayout;
import com.ramotion.circlemenu.CircleMenuView;

import static com.example.appdemo.animationutils.CustomAnimations.fadeOutView;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    private CircleMenuView mCircleMenuView;
    private CustomAnimations mCustomAnimations;
    private FilterMenuLayout mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mCircleMenuView = findViewById(R.id.main_circle_menu);
        mCustomAnimations = new CustomAnimations();

//        circleMenuListener();
        mLayout = (FilterMenuLayout) findViewById(R.id.filter_menu);
        final FilterMenu menu = new FilterMenu.Builder(this)
                .inflate(R.menu.filter_menu_items)//inflate  menu resource
                .attach(mLayout)
                .withListener(new FilterMenu.OnMenuChangeListener() {
                    @Override
                    public void onMenuItemClick(View view, int position) {
                        Log.d(TAG, "onButtonClickAnimationEnd| index: " + position);
                        switch (position) {
                            case 0:
//                                mCustomAnimations.presentActivity(MainActivity.this, ImagePickerActivity.class, view);
                                Intent intent = new Intent(MainActivity.this, ImagePickerActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                mLayout.setVisibility(View.GONE);
                                break;

                            case 1:
                                Toast.makeText(MainActivity.this, "Take a photo", Toast.LENGTH_SHORT).show();
                                break;
                        }

                    }

                    @Override
                    public void onMenuCollapse() {

                        Log.d(TAG, "onMenuCollapse: ");
                    }

                    @Override
                    public void onMenuExpand() {
                        Log.d(TAG, "onMenuExpand: ");
                    }
                })
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLayout.setVisibility(View.VISIBLE);
    }

    private void circleMenuListener() {
        mCircleMenuView.setEventListener(new CircleMenuView.EventListener() {
            @Override
            public void onMenuOpenAnimationStart(@NonNull CircleMenuView view) {
                Log.d(TAG, "onMenuOpenAnimationStart");
            }

            @Override
            public void onMenuOpenAnimationEnd(@NonNull CircleMenuView view) {
                Log.d(TAG, "onMenuOpenAnimationEnd");
            }

            @Override
            public void onMenuCloseAnimationStart(@NonNull CircleMenuView view) {
                Log.d(TAG, "onMenuCloseAnimationStart");
            }

            @Override
            public void onMenuCloseAnimationEnd(@NonNull CircleMenuView view) {
                Log.d(TAG, "onMenuCloseAnimationEnd");
            }

            @Override
            public void onButtonClickAnimationStart(@NonNull CircleMenuView view, int index) {
                Log.d(TAG, "onButtonClickAnimationStart| index: " + index);
            }

            @Override
            public void onButtonClickAnimationEnd(@NonNull CircleMenuView view, int index) {
                Log.d(TAG, "onButtonClickAnimationEnd| index: " + index);
                switch (index) {
                    case 0:
//                        Intent intent = new Intent(MainActivity.this, ImagePickerActivity.class);
//                        startActivity(intent);
                        mCustomAnimations.presentActivity(MainActivity.this, ImagePickerActivity.class, view);
                        break;

                    case 1:
                        Toast.makeText(MainActivity.this, "Index 1", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }


}
