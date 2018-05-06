package com.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.List;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    private int idGlobal = -1;
    private String accountGlobal = "Tap to log in";
    private String nicknameGlobal = "";
    private TextView nicknameField, accountField;
    private SharedPreferences preferences;
    private NavigationView navigationView;
    private View header;
    private ImageView favorite;

    private String[] categoryList, comodityName;
    private Spinner spinner;
    private String url_json;
    private String resultCode;
    private String msg;
    private String selectedCategory;

    private List<String> brandName = new ArrayList<>();
    private List<String> commodityName = new ArrayList<>();
    private List<String> imageURLs = new ArrayList<>();
    private List<Integer> idList = new ArrayList<>();

    private List<Integer> imgList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Favorite Button
        favorite = findViewById(R.id.favorite);

        //read color info
        SharedPreferences preferences;
        preferences = this.getSharedPreferences("DATA", Context.MODE_PRIVATE);
        String color = preferences.getString("theme", "bluebutton");
        if (color.equals("bluebutton"))
        {
            favorite.setImageResource(R.drawable.main_page_favorite_list_entrace_blue);
        } else {
            favorite.setImageResource(R.drawable.main_page_favorite_list_entrace_pink);
        }
        favorite.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                goToFavorite();
            }
        });

        //set category list on toolbar
        categoryList = new String[] {"All Kinds", "sunglasses", "glassframe", "blush"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner = findViewById(R.id.tool_bar_spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                selectedCategory = spinner.getSelectedItem().toString();

                if(selectedCategory.equals("All Kinds"))
                {
                    url_json = "http://18.219.212.60:8080/tio_backend/commodity/listAll";
                    getJsonObjectList(url_json);
                } else if(selectedCategory.equals("sunglasses"))
                {
                    url_json = "http://18.219.212.60:8080/tio_backend/commodity/list?categoryName=" + selectedCategory;
                    getJsonObjectList(url_json);
                } else if(selectedCategory.equals("glassframe"))
                {
                    url_json = "http://18.219.212.60:8080/tio_backend/commodity/list?categoryName=" + selectedCategory;
                    getJsonObjectList(url_json);
                } else if(selectedCategory.equals("blush"))
                {
                    url_json = "http://18.219.212.60:8080/tio_backend/commodity/list?categoryName=" + selectedCategory;
                    getJsonObjectList(url_json);
                } else {
                    url_json = "http://18.219.212.60:8080/tio_backend/commodity/listAll";
                    getJsonObjectList(url_json);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.home);

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
        //imageField = header.findViewById(R.id.imageView);

        //Tap the navigation bar
        nicknameField.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(idGlobal == -1){
                    goToLogin(); //If not logged in (-1), go to Login
                } else {
                    goToSetting(); //If logged in, go to Setting
                }
            }
        });
        accountField.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(idGlobal == -1){
                    goToLogin(); //If not logged in (-1), go to Login
                } else {
                    goToSetting(); //If logged in, go to Setting
                }
            }
        });
    }

    public void getJsonObjectList(String destination)
    {
        RequestQueue mQueue = Volley.newRequestQueue(this);

        String s = destination;

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, s,
                        new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject response)
                            {
                                try {
                                    JSONObject json = new JSONObject(response.toString());
                                    resultCode = json.getString("code");

                                    if(resultCode.equals("0"))
                                    {
                                        GridView gridview = findViewById(R.id.gridview);

                                        JSONArray array = json.getJSONArray("data");
                                        int count = array.length();
                                        JSONObject[] commodities = new JSONObject[count];

                                        if(commodityName.size() > 0)
                                        {
                                            commodityName.clear();
                                            brandName.clear();
                                            imageURLs.clear();
                                            imgList.clear();
                                            idList.clear();
                                        }
                                        for (int i=0; i<count; i++)
                                        {
                                            commodities[i] = array.getJSONObject(i);
                                        }

                                        String cName = "";
                                        String bName = "";
                                        String iURL = "";
                                        Integer id = 0;

                                        for (int i=0; i<count; i++){
                                            cName = commodities[i].getString("commodity_name");
                                            bName = commodities[i].getString("brand_name");
                                            iURL = commodities[i].getString("commodity_desc_img");
                                            id = commodities[i].getInt("commodity_id");

                                            commodityName.add(cName);
                                            brandName.add(bName);
                                            imageURLs.add(iURL);
                                            idList.add(id);
                                        }

                                        for (String s: commodityName)
                                        {
                                            int imageId = getResources().getIdentifier(
                                                    s,"drawable", getPackageName());
                                            imgList.add(imageId);
                                        }

                                        GridAdapter adapter = new GridAdapter(NavigationActivity.this,
                                                R.layout.grid_items,
                                                imgList,
                                                brandName,
                                                commodityName,
                                                imageURLs
                                        );

                                        adapter.notifyDataSetChanged();

                                        gridview.setAdapter(adapter);
                                        gridview.invalidateViews();

                                        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener()
                                        {
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                                            {
                                                goToCamera(position);
                                            }
                                        });

                                    }
                                    else {
                                        msg = json.getString("msg");
                                        //User Feedback
                                        AlertDialog.Builder dl = new AlertDialog.Builder(NavigationActivity.this);
                                        dl.setTitle("code:" + resultCode + ", msg:" + msg);
                                        dl.setMessage(msg);
                                        dl.setPositiveButton("OK", new DialogInterface.OnClickListener()
                                        {
                                            public void onClick(DialogInterface dialog, int which)
                                            {
                                                dialog.dismiss();
                                            }
                                        });
                                        dl.show();
                                    }
                                }
                                catch (JSONException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
                            }
                        }
                );
        mQueue.add(jsonObjectRequest);
    }

    //Always being called when returned to the home screen
    @Override
    public void onResume()
    {
        super.onResume();  // Always call the superclass method first

        //Read Login Info
        preferences = getSharedPreferences("DATA", Context.MODE_PRIVATE);
        idGlobal = preferences.getInt("id", -1);
        accountGlobal = preferences.getString("account", "Tap to log in");
        nicknameGlobal = preferences.getString("nickname", "");

        //Display Login Info
        navigationView = findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.home);
        header = navigationView.getHeaderView(0);
        nicknameField = header.findViewById(R.id.nickname1);
        nicknameField.setText(nicknameGlobal);
        accountField = header.findViewById(R.id.account1);
        accountField.setText(accountGlobal);
    }

    public void goToFavorite()
    {
        if(idGlobal == -1)
        {
            //User Feedback
            AlertDialog.Builder dl = new AlertDialog.Builder(NavigationActivity.this);
            dl.setTitle("Please login to use this feature.");
            dl.setMessage(msg);
            dl.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.dismiss();
                }
            });
            dl.show();
        }
        else {
            NavigationActivity.this.onPause();
            Intent intent = new Intent(this, FavoriteActivity.class);
            startActivity(intent);
        }
    }

    public void goToCamera(int p)
    {
        NavigationActivity.this.onPause();
        Intent intent = new Intent(getApplication(), CameraActivity.class);

        intent.putExtra("commodityID", idList.get(p));
        intent.putExtra("categoryName", selectedCategory);

        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //if home is selected, stay the same activity,
        //if privacy is selected, go to privacy activity,
        //if logout is selected, logout if logged in.
        if (id == R.id.home)
        {
        } else if (id == R.id.privacy)
        {
            NavigationActivity.this.onPause();
            Intent intent = new Intent(this, PrivacyActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.logout) {
            if (preferences.getInt("id", -1) != -1)
            {
                logout();
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("You have already logged out.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                            }
                        });
                builder.show();
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void goToLogin()
    {
        NavigationActivity.this.onPause();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void goToSetting()
    {
        NavigationActivity.this.onPause();
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    public void logout()
    {
        //Edit Login Info
        preferences = getSharedPreferences("DATA",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        //User Feedback
        AlertDialog.Builder dl = new AlertDialog.Builder(NavigationActivity.this);
        dl.setTitle("You have logged out.");
        dl.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
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
