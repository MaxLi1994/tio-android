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

public class StartActivity extends AppCompatActivity implements View.OnClickListener
{
    private ImageView male, glass1, glass2, blueBackground, female, lip1, lip2, pinkBackground;
    private ObjectAnimator objectAnimator;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //get theme (blue or pink) if already chosen
        preferences = getSharedPreferences("DATA", Context.MODE_PRIVATE);
        String theme = preferences.getString("theme", "notSelected");

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

        //set female on the left
        female.setX(-1050);
        lip1.setX(-1050);
        lip2.setX(-1050);
        pinkBackground.setVisibility(View.INVISIBLE);

        //set the initial position
        objectAnimator = ObjectAnimator.ofFloat(male, "x", 0);
        objectAnimator = ObjectAnimator.ofFloat(glass1, "x", 0);
        objectAnimator = ObjectAnimator.ofFloat(glass2, "x", -1050, -1050);

        //draw the pictures and start animation
        objectAnimator.start();
        startAnimation();
    }

    //when button is clicked
    public void onClick(View v)
    {
        int id = v.getId();

        //save the choice to preference
        if(id == R.id.buttonBlue)
        {
            //set theme blue
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("theme", "bluebutton");
            editor.apply();
        }
        else {
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

    //onCreate, start the animation
    public void startAnimation()
    {
        objectAnimator.addListener(new AnimatorListenerAdapter()
        {
            //when one animation ends, start another animation
            @Override
            public void onAnimationEnd(Animator animation)
            {
                fromCenterToRight(glass1);
                fromLeftToCenter(glass2);

                objectAnimator.addListener(new AnimatorListenerAdapter()
                {
                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        fromCenterToRight(glass2);
                        fromCenterToRight(male);
                        fadeout(blueBackground);

                        fadein(pinkBackground);
                        fromLeftToCenter(lip1);
                        fromLeftToCenter(female);

                        objectAnimator.addListener(new AnimatorListenerAdapter()
                        {
                            @Override
                            public void onAnimationEnd(Animator animation)
                            {
                                fromCenterToRight(lip1);
                                fromLeftToCenter(lip2);

                                //when all of the animation ends, go back and iterate the startAnimation()
                                objectAnimator.addListener(new AnimatorListenerAdapter()
                                {
                                    @Override
                                    public void onAnimationEnd(Animator animation)
                                    {
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

    //function to move ImageView from center to right
    private void fromCenterToRight(ImageView iv)
    {
        //set iv to the center
        iv.setX(0);

        //move iv to right
        objectAnimator = ObjectAnimator.ofFloat(iv, "x", 1050);

        //set duration
        objectAnimator.setDuration(1500);

        //start animation
        objectAnimator.start();
    }

    //function to move ImageView from left to center
    private void fromLeftToCenter(final ImageView iv)
    {
        //set iv to the center
        iv.setX(-1050);

        //move iv to right
        objectAnimator = ObjectAnimator.ofFloat(iv, "x", 0);

        //set duration
        objectAnimator.setDuration(1500);

        //start animation
        objectAnimator.start();
    }

    //function to fadeout ImageView
    private void fadeout(ImageView imageView)
    {
        //change transparency from 1 to 0
        AlphaAnimation alphaFadeout = new AlphaAnimation(1.0f, 0.0f);

        //set time for animation in msec
        alphaFadeout.setDuration(1500);

        //after animation, keep the state
        alphaFadeout.setFillAfter(true);

        imageView.startAnimation(alphaFadeout);
    }

    //function to fadein ImageView
    private void fadein(ImageView imageView)
    {
        //change transparency from 0 to 1
        AlphaAnimation alphaFadeIn = new AlphaAnimation(0.0f, 1.0f);

        //set time for animation in msec
        alphaFadeIn.setDuration(1500);

        //after animation, keep the state
        alphaFadeIn.setFillAfter(true);

        imageView.startAnimation(alphaFadeIn);
    }
}
