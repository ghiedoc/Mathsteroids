package com.appdev.kez.mathsteroids;

import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.MainThread;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.thekhaeng.pushdownanim.PushDownAnim;

public class MainActivity extends AppCompatActivity {

    Button playBtn, exitBtn;
    ImageView ivAbout, ivSound;
    MediaPlayer mediaPlayer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Fullscreen
         */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        playBtn = findViewById(R.id.playBtn);
        exitBtn = findViewById(R.id.exitBtn);
        ivAbout = findViewById(R.id.ivAbout);
        ivSound = findViewById(R.id.ivSound);

        PushDownAnim.setPushDownAnimTo(playBtn)
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Play", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, com.appdev.kez.mathsteroids.GameActivity.class);
                startActivity(intent);
            }
        });

        PushDownAnim.setPushDownAnimTo(exitBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Are you sure you want to exit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        /**
         * Moving Background on the Main Activity in loop
         */
        final ImageView backgroundOne = findViewById(R.id.start_bg_one);
        final ImageView backgroundTwo = findViewById(R.id.start_bg_two);

        final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(10000L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                final float width = backgroundOne.getWidth();
                final float translationX = width * progress;
                backgroundOne.setTranslationX(translationX);
                backgroundTwo.setTranslationX(translationX - width);
            }
        });
        animator.start();

    }
}
