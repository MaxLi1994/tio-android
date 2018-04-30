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
import android.media.Image;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private int idGlobal = -1;
    private String accountGlobal = "Tap to log in";
    private String nicknameGlobal = "";
    private TextView nicknameField, accountField;
    private ImageView imageField;
    private GridView test;
    private SharedPreferences preferences;
    private NavigationView navigationView;
    private View header;
    private ImageView favorite;

    private String[] categoryList, comodityName;
    private String imageURL;
    private Spinner spinner;

    private TextView textView;
    private String url_json;
    private String resultCode;
    private String msg;

    private List<String> brandName = new ArrayList<>();
    private List<String> commodityName = new ArrayList<>();
    private List<String> imageURLs = new ArrayList<>();

    // Resource IDを格納するarray
    private List<Integer> imgList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        //setContentView(R.layout.content_navigation);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Favorite Button
        favorite = findViewById(R.id.favorite);
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToFavorite();
            }
        });

        //set category list on toolbar
        categoryList = new String[] {"All Kinds", "sunglass", "lipstick", "hat", "watch"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner = findViewById(R.id.tool_bar_spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedCategory = spinner.getSelectedItem().toString();

                if(selectedCategory.equals("All Kinds")) {
                    url_json = "http://18.219.212.60:8080/tio_backend/commodity/listAll";
                    getJsonObject(url_json);
                } else if(selectedCategory.equals("sunglass")) {
                    url_json = "http://18.219.212.60:8080/tio_backend/commodity/list?categoryName=" + selectedCategory;
                    getJsonObject(url_json);
                } else if(selectedCategory.equals("lipstick")) {
                    url_json = "http://18.219.212.60:8080/tio_backend/commodity/list?categoryName=" + selectedCategory;
                    getJsonObject(url_json);
                } else if(selectedCategory.equals("hat")) {
                    url_json = "http://18.219.212.60:8080/tio_backend/commodity/list?categoryName=" + selectedCategory;
                    getJsonObject(url_json);
                } else if(selectedCategory.equals("watch")) {
                    url_json = "http://18.219.212.60:8080/tio_backend/commodity/list?categoryName=lip_stick" + selectedCategory;
                    getJsonObject(url_json);
                } else {
                    url_json = "http://18.219.212.60:8080/tio_backend/commodity/listAll";
                    getJsonObject(url_json);
                }

//                if(selectedCategory.equals("All Kinds")){
//                    // for-each brandNameをR.drawable.名前としてintに変換してarrayに登録
//                    for (String s: brandName){
//                        int imageId = getResources().getIdentifier(
//                                s,"drawable", getPackageName());
//                        imgList.add(imageId);
//                    }
//
//                    // GridViewのインスタンスを生成
//                    GridView gridview = findViewById(R.id.gridview);
//                    // BaseAdapter を継承したGridAdapterのインスタンスを生成
//                    // 子要素のレイアウトファイル grid_items.xml を
//                    // activity_main.xml に inflate するためにGridAdapterに引数として渡す
//                    GridAdapter adapter = new GridAdapter(NavigationActivity.this,
//                            R.layout.grid_items,
//                            imgList,
//                            brandName,
//                            commodityName
//                    );
//
//                    // gridViewにadapterをセット
//                    gridview.setAdapter(adapter);
//
//                } else if(selectedCategory.equals("Sunglasses")) {
//                    brandName = new String[]{"GUCCI"};
//                    photos = new int[]{R.drawable.glass};
//                    commodityName = new String[]{"black sunglasses ABC"};
//                    updateListView();
//                } else if(selectedCategory.equals("Lipsticks")){
//                    brandName = new String[]{"RAY BAN","CHANEL"};
//                    photos = new int[]{R.drawable.lip,R.drawable.watch};
//                    commodityName = new String[]{"WC-White2349","UP-23987"};
//                    updateListView();
//                } else if(selectedCategory.equals("Hats")) {
//                    brandName = new String[]{"LOUIS VUITTON"};
//                    photos = new int[]{R.drawable.hat};
//                    commodityName = new String[]{"ARG-GOLD34"};
//                    updateListView();
//                } else if(selectedCategory.equals("Watches")){
//                    brandName = new String[]{"CHANEL"};
//                    photos = new int[]{R.drawable.watch};
//                    commodityName = new String[]{"UP-23987"};
//                    updateListView();
//                }

//                //Feedback
//                AlertDialog.Builder dl = new AlertDialog.Builder(NavigationActivity.this);
//                dl.setTitle(selectedCategory);
//                dl.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                dl.show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
        imageField = header.findViewById(R.id.imageView);

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
        imageField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(idGlobal == -1){
                    goToLogin(); //If not logged in (-1), go to Login
                } else {
                    goToSetting(); //If logged in, go to Setting
                }
            }
        });

        // nameからメルアド作成
//        for(int i=0; i< brandName.length ;i++ ){
//            commodityName[i] = brandName[i];
//        }
    }

    public void getJsonObject(String destination) {
        //HTTPリクエストを行う Queue を生成する
        RequestQueue mQueue = Volley.newRequestQueue(this);

        //JSON用URL
        String s = destination;

        //JSONでGET
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, s,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONObject json = new JSONObject(response.toString());
                                    resultCode = json.getString("code");

                                    if(resultCode.equals("0")){
                                        JSONArray array = json.getJSONArray("data");
                                        int count = array.length();
                                        JSONObject[] commodities = new JSONObject[count];

                                        for (int i=0; i<count; i++){
                                            commodities[i] = array.getJSONObject(i);
                                        }

                                        String cName = "";
                                        String bName = "";
                                        String iURL = "";
                                        Integer id = 0;

                                        for (int i=0; i<count; i++){
                                            cName = commodities[i].getString("commodity_name");
                                            bName = commodities[i].getString("brand_name");
                                            iURL = commodities[i].getString("brand_logo");
                                            id = commodities[i].getInt("commodity_id");

                                            commodityName.add(cName);
                                            brandName.add(bName);
                                            imageURLs.add(iURL);
                                        }

                                        // for-each brandNameをR.drawable.名前としてintに変換してarrayに登録
                                        for (String s: commodityName){
                                            int imageId = getResources().getIdentifier(
                                                    s,"drawable", getPackageName());
                                            imgList.add(imageId);
                                        }

                                        // GridViewのインスタンスを生成
                                        GridView gridview = findViewById(R.id.gridview);

                                        // BaseAdapter を継承したGridAdapterのインスタンスを生成
                                        // 子要素のレイアウトファイル grid_items.xml を
                                        // activity_main.xml に inflate するためにGridAdapterに引数として渡す
                                        GridAdapter adapter = new GridAdapter(NavigationActivity.this,
                                                R.layout.grid_items,
                                                imgList,
                                                brandName,
                                                commodityName,
                                                imageURLs
                                        );

                                        // gridViewにadapterをセット
                                        gridview.setAdapter(adapter);

                                        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                goToCamera(position);
                                            }
                                        });

//                                        //User Feedback
//                                        AlertDialog.Builder dl = new AlertDialog.Builder(NavigationActivity.this);
//                                        dl.setTitle("code:" + resultCode);
//                                        dl.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                dialog.dismiss();
//                                                NavigationActivity.this.finish();//カテゴリ画面へ戻る
//                                            }
//                                        });
//                                        dl.show();
                                        /*
                                        preferences = getSharedPreferences("DATA", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putInt("id", id);
                                        editor.putString("account", account);
                                        editor.putString("nickname", nickname);
                                        editor.apply();
                                        */
                                    } else {
                                        msg = json.getString("msg");
                                        //User Feedback
                                        AlertDialog.Builder dl = new AlertDialog.Builder(NavigationActivity.this);
                                        dl.setTitle("code:" + resultCode + ", msg:" + msg);
                                        dl.setMessage(msg);
                                        dl.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                        dl.show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO: Handle error here
                            }
                        }
                );
        mQueue.add(jsonObjectRequest);
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

//    public void updateListView(){
//
//        // ListViewのインスタンスを生成
//        ListView listView = findViewById(R.id.listView);
//
//        // BaseAdapter を継承したadapterのインスタンスを生成
//        // レイアウトファイル list_items.xml を
//        // activity_main.xml に inflate するためにadapterに引数として渡す
//        BaseAdapter ba = new TestAdapter(this.getApplicationContext(),
//                R.layout.list_items, brandName, commodityName, photos);
//
//        // ListViewにadapterをセット
//        listView.setAdapter(ba);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//
//                ListView listView = (ListView) parent;
//                // クリックされたアイテムを取得します
//                String item = listView.getItemAtPosition(position).toString();
//
//                //Feedback
//                AlertDialog.Builder dl = new AlertDialog.Builder(NavigationActivity.this);
//                dl.setTitle(item);
//                dl.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                dl.show();
//
//                goToCamera();
//            }
//        });
//    }

    public void goToFavorite(){
        NavigationActivity.this.onPause();
        Intent intent = new Intent(this, FavoriteActivity.class);
        startActivity(intent);
    }

    public void goToCamera(int p){
        NavigationActivity.this.onPause();
        Intent intent = new Intent(this, CameraActivity.class);
        intent.putExtra("id", imgList.get(p));//IDを渡す
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
        preferences = getSharedPreferences("DATA",Context.MODE_PRIVATE);
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
