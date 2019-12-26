package com.appdev.kez.mathsteroids;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * For Sound Effects clicking
 */
public class SoundPlayer {

    private static SoundPool soundPool;
    private static int hitSound;
    private static int overSound;
    private static int winSound;
    public static int clickSound;

    public SoundPlayer(Context context){

        //SoundPool (int maxStrams, int streamType, int srcQuality)
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);

        hitSound = soundPool.load(context, R.raw.shoot, 1);
        overSound = soundPool.load(context, R.raw.failed,1);
        winSound = soundPool.load(context, R.raw.win,1);
        clickSound = soundPool.load(context, R.raw.click, 1);

    }

    public void playHitSound(){

        soundPool.play(hitSound, 1.0f, 1.0f, 1, 0,  1.0f);
    }

    public void playWinSound(){

        soundPool.play(winSound, 1.0f, 1.0f, 1, 0,  1.0f);
    }

    public void playOverSound(){

        soundPool.play(overSound, 1.0f, 1.0f, 1, 0,  1.0f);
    }

    public void playClicked(){

        soundPool.play(clickSound, 1.0f, 1.0f, 1, 0,  1.0f);
    }

}
