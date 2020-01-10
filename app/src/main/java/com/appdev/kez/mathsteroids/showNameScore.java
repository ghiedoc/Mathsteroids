package com.appdev.kez.mathsteroids;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class showNameScore extends AppCompatActivity {
    TextView tvShow;
    public static final String MY_PREFS_FILENAME = "com.appdev.kez.mathsteroids.Names";
    String top1, top2, top3;
    int score1, score2, score3;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_name_score);


        tvShow = (TextView) findViewById(R.id.tvShow);

        String st = getIntent().getExtras().getString("value");
        int sc = getIntent().getExtras().getInt("score");

        prefs = getSharedPreferences(MY_PREFS_FILENAME, MODE_PRIVATE);

        top1 = prefs.getString("top1", null);
        score1 = prefs.getInt("score1", 0);

        top2 = prefs.getString("top2", null);
        score2 = prefs.getInt("score2", 0);
        top3 = prefs.getString("top3", null);
        score3 = prefs.getInt("score3", 0);

        updatePrefs(st, sc);

        top1 = prefs.getString("top1", null);
        score1 = prefs.getInt("score1", 0);

        top2 = prefs.getString("top2", null);
        score2 = prefs.getInt("score2", 0);
        top3 = prefs.getString("top3", null);
        score3 = prefs.getInt("score3", 0);


        /**
         * REPLACE IF THERE IS A HIGH SCORE
         */

//        if (lastscore > top3) {
//            top3 = lastscore;
//            SharedPreferences.Editor editor = sp.edit();
//            editor.putInt("TOP 3", top3);
//            editor.apply();
//        }
//        if (lastscore > top2) {
//            int temp = top2;
//            top2 = lastscore;
//            top3 = temp;
//            SharedPreferences.Editor editor = sp.edit();
//            editor.putInt("TOP 3", top3);
//            editor.putInt("TOP 2", top2);
//            editor.apply();
//        }
//        if (lastscore > top1) {
//            int temp = top1;
//            top2 = lastscore;
//            top3 = temp;
//            SharedPreferences.Editor editor = sp.edit();
//            editor.putInt("TOP 2", top2);
//            editor.putInt("TOP 1", top1);
//            editor.apply();
//        }
        /**
         * DISPLAY THE SCORES
         */
        tvShow.setText("TOP 1 : " + top1.concat(Integer.toString(score1)) + "\n" +
                "TOP 2: " + top2 + score2 + "\n" +
                "TOP 3: " + top3 + score3 + "\n");


    }


    public void updatePrefs(String name, int score) {
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_FILENAME, MODE_PRIVATE).edit();
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
            editor.putString("top1", name);
            editor.putInt("score1", score);
            editor.commit();
        } else if (score2 < score) {
            editor.putString("top2", name);
            editor.putInt("score2", score);
            editor.commit();
        } else if (score3 < score) {
            editor.putString("top3", name);
            editor.putInt("score3", score);
            editor.commit();
        } else {

        }

    }
}
