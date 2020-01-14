package com.appdev.kez.mathsteroids;

import android.animation.ValueAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.MainThread;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.thekhaeng.pushdownanim.PushDownAnim;

public class MainActivity extends AppCompatActivity {
    private SoundPlayer sound;
    Button playBtn, exitBtn;
    ImageView ivAbout, ivSound, ivStar;
    HomeWatcher mHomeWatcher;
    int musicCounter = 0;
    Animation frombutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Fullscreen
         */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        sound = new SoundPlayer(this);
        playBtn = findViewById(R.id.playBtn);
        exitBtn = findViewById(R.id.exitBtn);
        ivAbout = findViewById(R.id.ivAbout);
        ivSound = findViewById(R.id.ivSound);
        ivStar = findViewById(R.id.ivStar);

        /**
         * transition button
         */
        frombutton = AnimationUtils.loadAnimation(this, R.anim.frombutton);
        playBtn.setAnimation(frombutton);
        exitBtn.setAnimation(frombutton);

        /**
         * score board activity
         */
        PushDownAnim.setPushDownAnimTo(ivStar)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sound.playClicked();
                        Toast.makeText(MainActivity.this, "Record", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), showNameScore.class);
                        startActivity(intent);
                    }
                });

        /**
         * play button
         */
        PushDownAnim.setPushDownAnimTo(playBtn)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sound.playClicked();
                        Toast.makeText(MainActivity.this, "Play", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, com.appdev.kez.mathsteroids.GameActivity.class);
                        startActivity(intent);
                    }
                });

        /**
         * exit button
         */
        PushDownAnim.setPushDownAnimTo(exitBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playClicked();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Are you sure you want to exit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(0);
//                                finish();
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

        /**
         * Background Music Service
         * BIND Music Service first
         */
        SharedPreferences loadToggleState = this.getSharedPreferences("MyMusic", Context.MODE_PRIVATE);
        musicCounter = loadToggleState.getInt("music", 0); //0 is the default value

        if (musicCounter == 0) {
            doBindService();
            Intent music = new Intent();
            music.setClass(this, BackgroundMusicService.class);
            startService(music);
        }


        /**
         * Start HomeWatcher
         */
        mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new HomeWatcher.OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                if (mServ != null) {
                    mServ.pauseMusic();
                }
            }

            @Override
            public void onHomeLongPressed() {
                if (mServ != null) {
                    mServ.pauseMusic();
                }
            }
        });
        mHomeWatcher.startWatch();

        /**
         * control sound by turning it off and on
         */
        PushDownAnim.setPushDownAnimTo(ivSound).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicCounter > 0) {
                    doBindService();
                    Intent music = new Intent();
                    music.setClass(MainActivity.this, BackgroundMusicService.class);
                    startService(music);
                    musicCounter = 0;
                    if (mServ != null) {
                        mServ.startMusic();
                        ivSound.setImageResource(R.drawable.sound_on_white);
                    }
                } else {
                    musicCounter = 1;
                    if (mServ != null) {
                        mServ.stopMusic();
                        ivSound.setImageResource(R.drawable.sound_off);
                    }
                }
                SharedPreferences saveMusic = getSharedPreferences("MyMusic", Context.MODE_PRIVATE);
                SharedPreferences.Editor editorMusic = saveMusic.edit();
                editorMusic.putInt("music", musicCounter);
                editorMusic.apply();
            }
        });

    }

    /**
     * Bind or Unbind the Background Music
     */
    private boolean mIsBound = false;
    private BackgroundMusicService mServ;

    /**
     * Background Music
     */
    private ServiceConnection Scon = new ServiceConnection() {

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((BackgroundMusicService.ServiceBinder) binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    void doBindService() {
        bindService(new Intent(this, BackgroundMusicService.class),
                Scon, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService() {
        if (mIsBound) {
            unbindService(Scon);
            mIsBound = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mServ != null) {
            mServ.resumeMusic();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        //Detect idle screen
        PowerManager pm = (PowerManager)
                getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = false;
        if (pm != null) {
            isScreenOn = pm.isScreenOn();
        }

        if (!isScreenOn) {
            if (mServ != null) {
                mServ.pauseMusic();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //UNBIND music service
        doUnbindService();
        Intent music = new Intent();
        music.setClass(this, BackgroundMusicService.class);
        stopService(music);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(0);
//                                finish();
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
}
