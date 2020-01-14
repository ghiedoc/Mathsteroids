package com.appdev.kez.mathsteroids;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thekhaeng.pushdownanim.PushDownAnim;

public class GameActivity extends AppCompatActivity {

    Dialog epicDialog, pauseDialog, nameDialog, highScoreDialog;
    ImageView closePopupPositiveImg, ivSound;
    TextView tvScore, tv1, tv2, tv3, tv4, tvQuestion, tvMessage, titleTv, messageTv, popUpScore, tvName, etSCore;
    EditText etName;
    ImageView iv1, iv2, iv3, iv4, ivPause;
    Button btnAccept, resumeBtn, restartBtn, exitBtn, submitBtn;
    Question question;
    Game g;
    Animation animation;
    Animation animation1;
    int score;
    HomeWatcher mHomeWatcher;
    int musicCounter = 0;

    //Initialize Class
    private SoundPlayer sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Fullscreen
         */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_game);

        sound = new SoundPlayer(this);

        epicDialog = new Dialog(this);
        pauseDialog = new Dialog(this);
        nameDialog = new Dialog(this);
        highScoreDialog = new Dialog(this);

        ivPause = findViewById(R.id.ivPause);
        ivSound = findViewById(R.id.ivSound);

        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.updown);
        animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.updown1);

        tvScore = findViewById(R.id.tvScore);
        tvQuestion = findViewById(R.id.tvQuestion);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);

        tvMessage = findViewById(R.id.tvMessage);

        iv1 = findViewById(R.id.iv1);
        iv2 = findViewById(R.id.iv2);
        iv3 = findViewById(R.id.iv3);
        iv4 = findViewById(R.id.iv4);

        iv1.setEnabled(false);
        iv2.setEnabled(false);
        iv3.setEnabled(false);
        iv4.setEnabled(false);

        etSCore = (TextView) findViewById(R.id.etScore);


        tvQuestion.setText("");
        tvScore.setText("0");
        startGame();

        View.OnClickListener answerButtonClickListener1 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int answerSelected = Integer.parseInt(tv1.getText().toString());
                g.checkAnswer(answerSelected);
                tvScore.setText(Integer.toString(g.getScore()));
                checkQuestionNum();
                sound.playHitSound();
                if (g.difficulty.equals("End")) {
                    showEnd();
                }
            }
        };

        View.OnClickListener answerButtonClickListener2 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int answerSelected = Integer.parseInt(tv2.getText().toString());
                g.checkAnswer(answerSelected);
                tvScore.setText(Integer.toString(g.getScore()));
                checkQuestionNum();
                sound.playHitSound();
                if (g.difficulty.equals("End")) {
                    showEnd();
                }
            }
        };
        View.OnClickListener answerButtonClickListener3 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int answerSelected = Integer.parseInt(tv3.getText().toString());
                g.checkAnswer(answerSelected);
                tvScore.setText(Integer.toString(g.getScore()));
                checkQuestionNum();
                sound.playHitSound();
                if (g.difficulty.equals("End")) {
                    showEnd();
                }
            }
        };
        View.OnClickListener answerButtonClickListener4 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int answerSelected = Integer.parseInt(tv4.getText().toString());
                g.checkAnswer(answerSelected);
                tvScore.setText(Integer.toString(g.getScore()));
                checkQuestionNum();
                sound.playHitSound();
                if (g.difficulty.equals("End")) {
                    showEnd();
                }

            }
        };

        iv1.setOnClickListener(answerButtonClickListener1);
        iv2.setOnClickListener(answerButtonClickListener2);
        iv3.setOnClickListener(answerButtonClickListener3);
        iv4.setOnClickListener(answerButtonClickListener4);

        PushDownAnim.setPushDownAnimTo(ivPause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPause();
            }
        });

        epicDialog = new Dialog(this);
        pauseDialog = new Dialog(this);
        nameDialog = new Dialog(this);
        highScoreDialog = new Dialog(this);

        /**
         * Moving Background on the Main Activity in loop
         */
        final ImageView backgroundOne = findViewById(R.id.game_bg_one);
        final ImageView backgroundTwo = findViewById(R.id.game_bg_two);

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
                    music.setClass(GameActivity.this, BackgroundMusicService.class);
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

    private void checkQuestionNum() {
        if (!g.difficulty.equals("End")) {
            if (g.getTotalQuestions() < 10) {
                nextTurn();
            } else if (g.getTotalQuestions() == 10 && g.getNumberCorrect() == 10) {
                g.changeDifficulty();
                if (!g.difficulty.equals("End")) {
                    showNextLevel();
                }
            } else if (g.getTotalQuestions() == 10 && g.getNumberCorrect() < 10) {
                showFail();
                iv1.setClickable(false);
                iv2.setClickable(false);
                iv3.setClickable(false);
                iv4.setClickable(false);
            }
        } else {
            showEnd();
            iv1.setClickable(false);
            iv2.setClickable(false);
            iv3.setClickable(false);
            iv4.setClickable(false);
        }
    }

    private void startGame() {
        iv2.setAnimation(animation1);
        iv1.setAnimation(animation);
        iv3.setAnimation(animation);
        iv4.setAnimation(animation1);

        iv1.setEnabled(true);
        iv2.setEnabled(true);
        iv3.setEnabled(true);
        iv4.setEnabled(true);

        iv1.setClickable(true);
        iv2.setClickable(true);
        iv3.setClickable(true);
        iv4.setClickable(true);

        tvScore.setText("0");
        g = new Game();
        nextTurn();
    }

    private void nextTurn() {
        if (!g.difficulty.equals("End")) {
            g.makeNewQuestion();
            int[] answer = g.getCurrentQuestion().getAnswerArray();

            tv1.setText(Integer.toString(answer[0]));
            tv2.setText(Integer.toString(answer[1]));
            tv3.setText(Integer.toString(answer[2]));
            tv4.setText(Integer.toString(answer[3]));

            iv1.setEnabled(true);
            iv2.setEnabled(true);
            iv3.setEnabled(true);
            iv4.setEnabled(true);

            tvQuestion.setText(g.getCurrentQuestion().getQuestionPhrase());
            tvMessage.setText(g.getNumberCorrect() + "/" + (g.getTotalQuestions() - 1));
        } else {
            showEnd();
        }
    }

    public void showNextLevel() {
        epicDialog.setContentView(R.layout.next_level_pop);
        epicDialog.setCancelable(false);
        closePopupPositiveImg = epicDialog.findViewById(R.id.closePopupPositiveImg);
        btnAccept = epicDialog.findViewById(R.id.btnAccept);
        popUpScore = epicDialog.findViewById(R.id.popUpScore);
        messageTv = epicDialog.findViewById(R.id.messageTv);
        titleTv = epicDialog.findViewById(R.id.titleTv);

        popUpScore.setText(g.difficulty);
        btnAccept.setText("Next");
        sound.playWinSound();

        PushDownAnim.setPushDownAnimTo(closePopupPositiveImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextTurn();
                epicDialog.dismiss();
            }
        });

        PushDownAnim.setPushDownAnimTo(btnAccept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextTurn();
                epicDialog.dismiss();
            }
        });

        epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        epicDialog.show();


    }

    public void showFail() {
        epicDialog.setContentView(R.layout.failed_level_pop);
        epicDialog.setCancelable(false);
        closePopupPositiveImg = epicDialog.findViewById(R.id.closePopupPositiveImg);
        btnAccept = epicDialog.findViewById(R.id.btnAccept);
        popUpScore = epicDialog.findViewById(R.id.popUpScore);
        messageTv = epicDialog.findViewById(R.id.messageTv);
        titleTv = epicDialog.findViewById(R.id.titleTv);
        titleTv.setText("Failed!");
        btnAccept.setText("Next");
        messageTv.setText("You've failed to accomplish the challenge!");
        popUpScore.setText(Integer.toString(g.getScore()));

        sound.playOverSound();

        PushDownAnim.setPushDownAnimTo(btnAccept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEnterName();
                epicDialog.dismiss();

            }
        });

        PushDownAnim.setPushDownAnimTo(closePopupPositiveImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameActivity.this, com.appdev.kez.mathsteroids.MainActivity.class);
                startActivity(intent);
            }
        });

        epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        epicDialog.show();


    }

    public void showEnd() {
        epicDialog.setContentView(R.layout.next_level_pop);
        epicDialog.setCancelable(false);
        closePopupPositiveImg = epicDialog.findViewById(R.id.closePopupPositiveImg);
        btnAccept = epicDialog.findViewById(R.id.btnAccept);
        popUpScore = epicDialog.findViewById(R.id.popUpScore);
        messageTv = epicDialog.findViewById(R.id.messageTv);
        titleTv = epicDialog.findViewById(R.id.titleTv);

        titleTv.setText("Congratulations!");
        btnAccept.setText("Next");
        messageTv.setText("You completed all the challenges!");
        popUpScore.setText(Integer.toString(g.getScore()));
        sound.playWinSound();

        PushDownAnim.setPushDownAnimTo(closePopupPositiveImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                epicDialog.dismiss();
            }
        });

        PushDownAnim.setPushDownAnimTo(btnAccept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEnterName();
                epicDialog.dismiss();

            }
        });

        epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        epicDialog.show();
    }

    public void showPause() {
        pauseDialog.setContentView(R.layout.pause_pop);
        pauseDialog.setCancelable(false);
        resumeBtn = pauseDialog.findViewById(R.id.resumeBtn);
        restartBtn = pauseDialog.findViewById(R.id.restartBtn);
        exitBtn = pauseDialog.findViewById(R.id.exitBtn);

        sound.playClicked();

        PushDownAnim.setPushDownAnimTo(resumeBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playClicked();
                pauseDialog.dismiss();
            }
        });

        PushDownAnim.setPushDownAnimTo(exitBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playClicked();
                Intent intent = new Intent(GameActivity.this, com.appdev.kez.mathsteroids.MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        PushDownAnim.setPushDownAnimTo(restartBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playClicked();
                startGame();
                pauseDialog.dismiss();
            }
        });

        pauseDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pauseDialog.show();
    }


    public void showEnterName() {
        nameDialog.setContentView(R.layout.high_score_pop);
        nameDialog.setCancelable(false);
        etSCore = nameDialog.findViewById(R.id.etScore);
        tvScore = nameDialog.findViewById(R.id.tvScore);
        etName = nameDialog.findViewById(R.id.etName);
        submitBtn = nameDialog.findViewById(R.id.submitBtn);
        nameDialog.show();

        tvScore.setText(Integer.toString(g.getScore()));


        PushDownAnim.setPushDownAnimTo(submitBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etName.getText().toString().trim().isEmpty()) {
                    if (!(etName.getText().toString().trim().length() > 5)) {
                        Intent intent = new Intent(getApplicationContext(), showNameScore.class);
                        String gname = etName.getText().toString().trim();
                        intent.putExtra("value", gname);
                        intent.putExtra("score", g.getScore());
                        startActivity(intent);
                        finish();
                        nameDialog.dismiss();
                    } else {
                        Toast.makeText(GameActivity.this, "Too Long(Help)", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(GameActivity.this, "Null Input", Toast.LENGTH_SHORT).show();
                }
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
        showPause();
    }
}

