package com.myapplication;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AdaptiveIconDrawable;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private int idGlobal = -1;
    private String accountGlobal = "Tap to log in";
    private String nicknameGlobal = "";
    private TextView nicknameField, accountField;
    private ListView test;
    private SharedPreferences preferences;
    private NavigationView navigationView;
    private View header;

    private String[] categoryList;
    private Spinner spinner;

    private static final String[] brandName = {
            "GUCCI",
            "LOUIS VUITTON",
            "RAY BAN",
            "CHANEL",
    };

    // drawableに画像を入れる、R.id.xxx はint型
    private static final int[] photos = {
            R.drawable.glass,
            R.drawable.hat,
            R.drawable.lip,
            R.drawable.watch
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        //setContentView(R.layout.content_navigation);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set category list on toolbar
        categoryList = new String[] {"All Kinds", "Sunglasses", "Lipsticks", "Hats", "Watches"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner = findViewById(R.id.tool_bar_spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = spinner.getSelectedItem().toString();
                //Feedback
                AlertDialog.Builder dl = new AlertDialog.Builder(NavigationActivity.this);
                dl.setTitle(selectedCategory);
                dl.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dl.show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //read color info
        preferences = getSharedPreferences("DATA", Context.MODE_PRIVATE);
        String color = preferences.getString("theme", "blue");
        //If logged in
        if (color.equals("blue")) {
            test = findViewById(R.id.listView);
            test.setBackgroundColor(Color.rgb(153, 217, 234));
        } else {
            test = findViewById(R.id.listView);
            test.setBackgroundColor(Color.rgb(255, 174, 201));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Read Login Info
        preferences = getSharedPreferences("DATA", Context.MODE_PRIVATE);
        idGlobal = preferences.getInt("id", -1);
        accountGlobal = preferences.getString("account", "Tap to log in");
        nicknameGlobal = preferences.getString("nickname", "");

        //Display Login Info
        navigationView = findViewById(R.id.nav_view);
        header = navigationView.getHeaderView(0);
        nicknameField = header.findViewById(R.id.nickname1);
        nicknameField.setText(nicknameGlobal);
        accountField = header.findViewById(R.id.account1);
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

        String[] commodityName = new String[]{
                "black sunglasses ABC",
                "ARG-GOLD34",
                "WC-White2349",
                "UP-23987",
        };

        // nameからメルアド作成
//        for(int i=0; i< brandName.length ;i++ ){
//            commodityName[i] = brandName[i];
//        }

        // ListViewのインスタンスを生成
        ListView listView = findViewById(R.id.listView);

        // BaseAdapter を継承したadapterのインスタンスを生成
        // レイアウトファイル list_items.xml を
        // activity_main.xml に inflate するためにadapterに引数として渡す
        BaseAdapter ba = new TestAdapter(this.getApplicationContext(),
                R.layout.list_items, brandName, commodityName, photos);

        // ListViewにadapterをセット
        listView.setAdapter(ba);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ListView listView = (ListView) parent;
                // クリックされたアイテムを取得します
                String item = listView.getItemAtPosition(position).toString();

                //Feedback
                AlertDialog.Builder dl = new AlertDialog.Builder(NavigationActivity.this);
                dl.setTitle(item);
                dl.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dl.show();

                goToCamera();
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
        navigationView = findViewById(R.id.nav_view);
        header = navigationView.getHeaderView(0);
        nicknameField = header.findViewById(R.id.nickname1);
        nicknameField.setText(nicknameGlobal);
        accountField = header.findViewById(R.id.account1);
        accountField.setText(accountGlobal);
    }

    public void goToCamera(){
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
        navigationView = findViewById(R.id.nav_view);
        header = navigationView.getHeaderView(0);
        nicknameField = header.findViewById(R.id.nickname1);
        nicknameField.setText(nicknameGlobal);
        accountField = header.findViewById(R.id.account1);
        accountField.setText(accountGlobal);
    }
}
