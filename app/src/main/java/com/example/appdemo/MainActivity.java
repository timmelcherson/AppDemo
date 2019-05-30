package com.example.appdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ramotion.circlemenu.CircleMenuView;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    private CircleMenuView mCircleMenuView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCircleMenuView = findViewById(R.id.main_circle_menu);

        circleMenuListener();
    }


    private void circleMenuListener() {
        mCircleMenuView.setEventListener(new CircleMenuView.EventListener(){
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
                        presentActivity(view);
                        break;

                    case 1:
                        Toast.makeText(MainActivity.this, "Index 1", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    public void presentActivity(View view) {
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, view, "transition");
        int revealX = (int) (view.getX() + view.getWidth() / 2);
        int revealY = (int) (view.getY() + view.getHeight() / 2);

        Intent intent = new Intent(this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(ImagePickerActivity.EXTRA_CIRCULAR_REVEAL_Y, revealY);

        ActivityCompat.startActivity(this, intent, options.toBundle());
    }

}
