package com.myapplication;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageView;
    private ObjectAnimator objectAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //Animation
        findViewById(R.id.imageView4).startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim1));
        findViewById(R.id.imageView5).startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim2));

        //Button
        findViewById(R.id.male).setOnClickListener(this);
        Button button = findViewById(R.id.male);
    }

    //@Override
    public void onClick(View v) {
        Intent intent = new Intent(this, NavigationActivity.class);
        startActivity(intent);
    }
}
