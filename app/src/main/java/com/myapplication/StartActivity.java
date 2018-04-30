package com.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView male, glass1, glass2, blueBackground, female, lip1, lip2, pinkBackground;
    private ObjectAnimator objectAnimator;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        preferences = getSharedPreferences("DATA", Context.MODE_PRIVATE);
        String theme = preferences.getString("theme", "notSelected");

//        if(theme.equals("notSelected")){
            //set listener on buttons
            findViewById(R.id.buttonBlue).setOnClickListener(this);
            findViewById(R.id.buttonPink).setOnClickListener(this);

            //set imageView
            male = findViewById(R.id.male);
            glass1 = findViewById(R.id.glass1);
            glass2 = findViewById(R.id.glass2);
            blueBackground = findViewById(R.id.blueBackground);

            female = findViewById(R.id.female);
            lip1 = findViewById(R.id.lip1);
            lip2 = findViewById(R.id.lip2);
            pinkBackground = findViewById(R.id.pinkBackground);

            //set female on left
            female.setX(-1050);
            lip1.setX(-1050);
            lip2.setX(-1050);
            pinkBackground.setVisibility(View.INVISIBLE);

            //initial position (duration 0)
            objectAnimator = ObjectAnimator.ofFloat(male, "x", 0);
            objectAnimator = ObjectAnimator.ofFloat(glass1, "x", 0);
            objectAnimator = ObjectAnimator.ofFloat(glass2, "x", -1050, -1050);
            objectAnimator.start();

            startAnimation();
//        } else {
            //move to next activity
//            Intent intent = new Intent(this, NavigationActivity.class);
//            startActivity(intent);
//            finish();
//        }
    }

    //when button is clicked
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.buttonBlue) {
            //set theme bluebutton
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("theme", "bluebutton");
            editor.apply();
        } else {
            //set theme pink
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("theme", "pink");
            editor.apply();
        }
        //move to next activity
        Intent intent = new Intent(this, NavigationActivity.class);
        startActivity(intent);
        finish();
    }

    public void startAnimation(){
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Animation 開始
                fromCenterToRight(glass1);
                fromLeftToCenter(glass2);

                // リスナーを設定（glass2が終わったらglass2とmaleを右へ、blueをフェードアウト）
                objectAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        fromCenterToRight(glass2);
                        fromCenterToRight(male);
                        fadeout(blueBackground);

                        fadein(pinkBackground);
                        fromLeftToCenter(lip1);
                        fromLeftToCenter(female);

                        // リスナーを設定（glass2が終わったらglass2とmaleを右へ、blueをフェードアウト）
                        objectAnimator.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {

                                fromCenterToRight(lip1);
                                fromLeftToCenter(lip2);
                                //                        fadeout(blueBackground);

                                // リスナーを設定（glass2が終わったらglass2とmaleを右へ、blueをフェードアウト）
                                objectAnimator.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        fromCenterToRight(lip2);
                                        fromCenterToRight(female);
                                        fadeout(pinkBackground);

                                        fadein(blueBackground);
                                        fromLeftToCenter(glass1);
                                        fromLeftToCenter(male);

                                        startAnimation();
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    private void fromCenterToRight(ImageView iv){
        //set iv to the center
        iv.setX(0);
        //move iv to right
        objectAnimator = ObjectAnimator.ofFloat(iv, "x", 1050);
        //set duration
        objectAnimator.setDuration(2000);
        //start animation
        objectAnimator.start();
    }

    private void fromLeftToCenter(final ImageView iv){
        //set iv to the center
        iv.setX(-1050);
        //move iv to right
        objectAnimator = ObjectAnimator.ofFloat(iv, "x", 0);
        //set duration
        objectAnimator.setDuration(2000);
        //start animation
        objectAnimator.start();
    }

    private void fadeout(ImageView imageView){
        // 透明度を1から0に変化
        AlphaAnimation alphaFadeout = new AlphaAnimation(1.0f, 0.0f);
        // animation時間 msec
        alphaFadeout.setDuration(2000);
        // animationが終わったそのまま表示にする
        alphaFadeout.setFillAfter(true);

        imageView.startAnimation(alphaFadeout);
    }

    private void fadein(ImageView imageView){
        // 透明度を0から1に変化
        AlphaAnimation alphaFadeIn = new AlphaAnimation(0.0f, 1.0f);
        // animation時間 msec
        alphaFadeIn.setDuration(2000);
        // animationが終わったそのまま表示にする
        alphaFadeIn.setFillAfter(true);

        imageView.startAnimation(alphaFadeIn);
    }
}
