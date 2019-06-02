package com.example.appdemo.animationutils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;

import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import com.example.appdemo.utils.Constants;

public class CustomAnimations {

    public void presentActivity(Activity activity, Class target, View view) {
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(activity, view, "transition");
        int revealX = (int) (view.getX() + view.getWidth() / 2);
        int revealY = (int) (view.getY() + view.getHeight() / 2);

        Intent intent = new Intent(activity, target);
        intent.putExtra(Constants.EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(Constants.EXTRA_CIRCULAR_REVEAL_Y, revealY);

        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    public void revealActivity(int x, int y, View view) {

        float finalRadius = (float) (Math.max(view.getWidth(), view.getHeight()) * 1.1);

        // create the animator for this view (the start radius is zero)
        Animator circularReveal = ViewAnimationUtils.createCircularReveal(view, x, y, 0, finalRadius);
        circularReveal.setDuration(200);
        circularReveal.setInterpolator(new AccelerateInterpolator());

        // make the view visible and start the animation
        view.setVisibility(View.VISIBLE);
        circularReveal.start();
    }

    public static void fadeInView(final View view) {
        view.setVisibility(View.VISIBLE);
        view.animate()
                .setDuration(200)
                .alpha(1.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.clearAnimation();

                    }
                });

    }
    public static void fadeOutView(final View view) {
        view.animate()
                .setDuration(200)
                .alpha(0.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.GONE);
                    }
                });
    }
}
