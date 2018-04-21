package com.myapplication;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button_blue;
    private Button button_pink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //Animation
        findViewById(R.id.imageView4).startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim1));
        findViewById(R.id.imageView5).startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim2));

        //Button
        button_blue = findViewById(R.id.male);
        button_blue.setOnClickListener(this);

        button_pink = findViewById(R.id.female);
        button_pink.setOnClickListener(this);
    }

    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.male) {
            Intent intent = new Intent(this, NavigationActivity.class);
            startActivity(intent);
            finish();
            System.out.println("des");
        } else {
            Intent intent2 = new Intent(this, StartActivity2.class);
            startActivity(intent2);
            finish();
        }
    }
}
