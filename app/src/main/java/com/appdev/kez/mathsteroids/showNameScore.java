package com.appdev.kez.mathsteroids;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thekhaeng.pushdownanim.PushDownAnim;

public class showNameScore extends AppCompatActivity {
    TextView tvShow;
    Button btnPlayAgain, btnMainMenu;
    public static final String MY_PREFS_FILENAME = "com.appdev.kez.mathsteroids.Names";
    String top1, top2, top3, st;
    int score1, score2, score3, sc;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        /**
//         * Fullscreen
//         */
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_show_name_score);


        prefs = getSharedPreferences(MY_PREFS_FILENAME, MODE_PRIVATE);


        top1 = prefs.getString("top1", "");
        score1 = prefs.getInt("score1", 0);
        top2 = prefs.getString("top2", "");
        score2 = prefs.getInt("score2", 0);
        top3 = prefs.getString("top3", "");
        score3 = prefs.getInt("score3", 0);

        tvShow = (TextView) findViewById(R.id.tvShow);
        btnPlayAgain = (Button) findViewById(R.id.btnPlayAgain);
        btnMainMenu = (Button) findViewById(R.id.btnMainMenu);

        try {
            st = getIntent().getExtras().getString("value");
            sc = getIntent().getExtras().getInt("score");
            arrange();
            updatePrefs(st, sc);
            arrange();
        } catch (Exception e) {
        }
        arrange();
        top1 = prefs.getString("top1", "");
        score1 = prefs.getInt("score1", 0);
        top2 = prefs.getString("top2", "");
        score2 = prefs.getInt("score2", 0);
        top3 = prefs.getString("top3", "");
        score3 = prefs.getInt("score3", 0);

        /**
         * DISPLAY THE SCORES
         */
        try {
            tvShow.setText("TOP 1 : " + top1 + " " + score1 + "\n" +
                    "TOP 2: " + top2 + " " + score2 + "\n" +
                    "TOP 3: " + top3 + " " + score3 + "\n");
        } catch (Exception e) {
            tvShow.setText("TOP 1 : null " + score1 + "\n" +
                    "TOP 2: null " + score2 + "\n" +
                    "TOP 3: null " + score3 + "\n");

        }

        PushDownAnim.setPushDownAnimTo(btnPlayAgain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(showNameScore.this, "Play", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(showNameScore.this, com.appdev.kez.mathsteroids.GameActivity.class);
                startActivity(intent);
                finish();
            }
        });

        PushDownAnim.setPushDownAnimTo(btnMainMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(showNameScore.this, "Main Menu", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(showNameScore.this, com.appdev.kez.mathsteroids.MainActivity.class);
                startActivity(intent);
                finish();
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


    public void updatePrefs(String name, int score) {
        editor = getSharedPreferences(MY_PREFS_FILENAME, MODE_PRIVATE).edit();
        if (top1 == null) {
            editor.putString("top1", name);
            editor.putInt("score1", score);
            editor.commit();

        } else if (top2 == null) {
            editor.putString("top2", name);
            editor.putInt("score2", score);
            editor.commit();

        } else if (top3 == null) {
            editor.putString("top3", name);
            editor.putInt("score3", score);
            editor.commit();
        } else {
            newHighPrefs(name, score);
        }
    }

    public void newHighPrefs(String name, int score) {
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_FILENAME, MODE_PRIVATE).edit();
        if (score1 < score) {
            String tempName = prefs.getString("top1", null);
            int tempScore = prefs.getInt("score1", 0);
            editor.putString("top1", name);
            editor.putInt("score1", score);
            name = tempName;
            score = tempScore;
            editor.commit();

        }
        if (score2 < score) {
            String tempName = prefs.getString("top2", null);
            int tempScore = prefs.getInt("score2", 0);
            editor.putString("top2", name);
            editor.putInt("score2", score);
            name = tempName;
            score = tempScore;
            editor.commit();
        }
        if (score3 < score) {
            editor.putString("top3", name);
            editor.putInt("score3", score);
            editor.commit();
        }
        arrange();
    }

    public void arrange() {
        editor = getSharedPreferences(MY_PREFS_FILENAME, MODE_PRIVATE).edit();

        if (prefs.getInt("score1", 0) < prefs.getInt("score2", 0)) {
            String tempName = prefs.getString("top1", null);
            int tempScore = prefs.getInt("score1", 0);
            editor.putString("top1", prefs.getString("top2", null));
            editor.putInt("score1", prefs.getInt("score2", 0));
            editor.putString("top2", tempName);
            editor.putInt("score2", tempScore);
            editor.commit();
        }

        if (prefs.getInt("score2", 0) < prefs.getInt("score3", 0)) {
            String tempName = prefs.getString("top2", null);
            int tempScore = prefs.getInt("score2", 0);
            editor.putString("top2", prefs.getString("top3", null));
            editor.putInt("score2", prefs.getInt("score3", 0));
            editor.putString("top3", tempName);
            editor.putInt("score3", tempScore);
            editor.commit();
        }


    }
}
