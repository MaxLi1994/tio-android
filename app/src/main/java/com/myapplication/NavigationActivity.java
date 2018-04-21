package com.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.internal.NavigationMenuPresenter;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private int idGlobal = -1;
    private String accountGlobal = "Tap to log in";
    private String nicknameGlobal = "";
    private TextView nicknameField;
    private TextView accountField;
    private SharedPreferences preferences;
    private NavigationView navigationView;
    private View header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
//        setContentView(R.layout.content_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "something if needed!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        findViewById(R.id.imageView6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToCamera();
            }
        });

        //Read Login Info
        preferences = getSharedPreferences("DATA", Context.MODE_PRIVATE);
        idGlobal = preferences.getInt("id", -1);
        accountGlobal = preferences.getString("account", "Tap to log in");
        nicknameGlobal = preferences.getString("nickname", "");

        //Display Login Info
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        header = navigationView.getHeaderView(0);
        nicknameField = (TextView)header.findViewById(R.id.nickname1);
        nicknameField.setText(nicknameGlobal);
        accountField = (TextView)header.findViewById(R.id.account1);
        accountField.setText(accountGlobal);

        //Tap the navigation bar
        nicknameField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(idGlobal == -1){
                    goToLogin(); //If not logged in (-1), go to Login
                } else {
                    goToSetting(); //If logged in, go to Setting
                }
            }
        });
        accountField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(idGlobal == -1){
                    goToLogin(); //If not logged in (-1), go to Login
                } else {
                    goToSetting(); //If logged in, go to Setting
                }
            }
        });
    }

    //Always being called when returned to the home screen
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        //Read Login Info
        preferences = getSharedPreferences("DATA", Context.MODE_PRIVATE);
        idGlobal = preferences.getInt("id", -1);
        accountGlobal = preferences.getString("account", "Tap to log in");
        nicknameGlobal = preferences.getString("nickname", "");

        //Display Login Info
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        header = navigationView.getHeaderView(0);
        nicknameField = (TextView)header.findViewById(R.id.nickname1);
        nicknameField.setText(nicknameGlobal);
        accountField = (TextView)header.findViewById(R.id.account1);
        accountField.setText(accountGlobal);
    }

    public void goToCamera(){
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        /*
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        */

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("test1")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                    // ボタンをクリックしたときの動作をここに書く
                        }
                    });
            builder.show();
        } else if (id == R.id.nav_gallery) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("test2")
                    .setPositiveButton("sure", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // ボタンをクリックしたときの動作をここに書く
                        }
                    });
            builder.show();
        } else if (id == R.id.nav_share) {
            logout();
        }
        /*else if (id == R.id.nav_manage) {
            goToSetting();
        } else if (id == R.id.nav_send) {
            goToLogin();
        } */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void goToLogin(){
        NavigationActivity.this.onPause();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void goToSetting(){
        NavigationActivity.this.onPause();
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    public void logout(){
        //Edit Login Info
        preferences =getSharedPreferences("DATA",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        //User Feedback
        AlertDialog.Builder dl = new AlertDialog.Builder(NavigationActivity.this);
        dl.setTitle("You have logged out.");
        dl.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dl.show();

        //Read Login Info
        preferences = getSharedPreferences("DATA", Context.MODE_PRIVATE);
        idGlobal = preferences.getInt("id", -1);
        accountGlobal = preferences.getString("account", "Tap to log in");
        nicknameGlobal = preferences.getString("nickname", "");

        //Display Login Info
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        header = navigationView.getHeaderView(0);
        nicknameField = (TextView)header.findViewById(R.id.nickname1);
        nicknameField.setText(nicknameGlobal);
        accountField = (TextView)header.findViewById(R.id.account1);
        accountField.setText(accountGlobal);
    }
}
