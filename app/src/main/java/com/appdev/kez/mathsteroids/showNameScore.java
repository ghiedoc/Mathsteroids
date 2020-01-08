package com.appdev.kez.mathsteroids;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class showNameScore extends AppCompatActivity {
    TextView tvShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_name_score);


        /**
         * SHOW THE NAME FROM THE TEXT VIEW
         */
//        tvShow = (TextView) findViewById(R.id.tvShow);
//        String st = getIntent().getExtras().getString("value");
//        tvShow.setText(st);

        /**
         *  LOAD OLD SCORES
         */
        int lastscore, top1, top2, top3;
        SharedPreferences sp = getSharedPreferences("PRESS", 0);
        lastscore = sp.getInt("LAST SCORE", 0);
        top1 = sp.getInt("TOP 1", 0);
        top2 = sp.getInt("TOP 2", 0);
        top3 = sp.getInt("TOP 3", 0);

        /**
         * REPLACE IF THERE IS A HIGH SCORE
         */

        if (lastscore > top3) {
            top3 = lastscore;
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("TOP 3", top3);
            editor.apply();
        }
        if (lastscore > top2) {
            int temp = top2;
            top2 = lastscore;
            top3 = temp;
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("TOP 3", top3);
            editor.putInt("TOP 2", top2);
            editor.apply();
        }
        if (lastscore > top1) {
            int temp = top1;
            top2 = lastscore;
            top3 = temp;
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("TOP 2", top2);
            editor.putInt("TOP 1", top1);
            editor.apply();
        }
        /**
         * DISPLAY THE SCORES
         */
        tvShow.setText("LAST SCORE: " + lastscore + "\n" +
                "TOP 1: " + top1 + "\n" +
                "TOP 2: " + top2 + "\n" +
                "TOP 3: "+ top3 + "\n");


    }
}
