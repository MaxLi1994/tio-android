package com.myapplication;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class StartActivity2 extends AppCompatActivity implements View.OnClickListener {

    private Button button_blue;
    private Button button_pink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start2);

        //Animation
        findViewById(R.id.imageView9).startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim4));
        findViewById(R.id.imageView8).startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim3));
        findViewById(R.id.imageView13).startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim5));

        //Button
        button_blue = findViewById(R.id.male);
        button_blue.setOnClickListener(this);

        button_pink = findViewById(R.id.female);
        button_pink.setOnClickListener(this);
    }

    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.female) {
            Intent intent = new Intent(this, NavigationActivity.class);
            startActivity(intent);
        } else {
            Intent intent2 = new Intent(this, StartActivity.class);
            startActivity(intent2);
        }
    }
}
