package com.appdev.kez.mathsteroids;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {

    ImageView logo;
    private static int splashTimeOut = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Fullscreen
         */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);

        logo = findViewById(R.id.logo);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splashanimation);
        logo.setAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        },splashTimeOut);
        Animation anim = AnimationUtils.loadAnimation(this,R.anim.splashanimation);
        logo.setAnimation(anim);

    }
}
