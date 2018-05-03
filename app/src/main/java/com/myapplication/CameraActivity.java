package com.myapplication;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.myapplication.graphic.Model2D;
import com.myapplication.graphic.ModelLoader;
import com.myapplication.graphic.ModelTuple;
import com.myapplication.vision.CameraController;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CameraActivity extends AppCompatActivity {

    private int commodityID, userID;
    private String url_json_commodity, url_json_category, urlForImage, resultCode, msg, categoryName, url_json_favorite;
    private String changeFavoritedURL, res;

    private List<String> brandName = new ArrayList<>();
    private List<String> commodityName = new ArrayList<>();
    private List<String> imageURLs = new ArrayList<>();
    private List<Integer> idList = new ArrayList<>();

    // Resource IDを格納するarray
    private List<Integer> imgList = new ArrayList<>();

    private String cName = "";
    private String bName = "";
    private String iURL = "";
    private Integer cid = 0;

    private Button moreInfoButton;

    private ImageView fab;
    private boolean favorited = false;

    private CameraController cameraController;
    private List<ModelTuple> modelTupleList;

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        Intent intent = getIntent();
        commodityID = intent.getIntExtra("commodityID", 0);
        categoryName = intent.getStringExtra("categoryName");

        url_json_commodity = "http://18.219.212.60:8080/tio_backend/commodity/detail?commodityId=" + commodityID;
        getJsonObject(url_json_commodity);

        if (categoryName.equals("All Kinds")) {
            url_json_category = "http://18.219.212.60:8080/tio_backend/commodity/listAll";
            getJsonObjectList(url_json_category);
        } else {
            url_json_category = "http://18.219.212.60:8080/tio_backend/commodity/list?categoryName=" + categoryName;
            getJsonObjectList(url_json_category);
        }

        //Read Login Info
        SharedPreferences preferences = getSharedPreferences("DATA", Context.MODE_PRIVATE);
        userID = preferences.getInt("id", -1);

        checkFavorite(commodityID, userID);

        //Favorite Button
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //If not logged in
                if(userID == -1){
                    //User Feedback
                    AlertDialog.Builder dl = new AlertDialog.Builder(CameraActivity.this);
                    dl.setTitle("Please login to use this feature.");
                    dl.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dl.show();
                } else {
                    //If logged in
                    updateFavorite(commodityID, userID);
                }
            }
        });

        //More Info Button
        moreInfoButton = findViewById(R.id.moreInfoButton);
        //read color info
        String color = preferences.getString("theme", "bluebutton");
        if (color.equals("bluebutton")) {
            Drawable btn_color = ResourcesCompat.getDrawable(getResources(), R.drawable.frame_style2, null);
            moreInfoButton.setBackground(btn_color);
        } else {
            Drawable btn_color = ResourcesCompat.getDrawable(getResources(), R.drawable.frame_style3, null);
            moreInfoButton.setBackground(btn_color);
        }
        moreInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), DetailActivity.class);

                intent.putExtra("commodityID", commodityID);

                startActivity(intent);
            }
        });


        // Vision part
        cameraController = new CameraController(this);

        // Check for the camera permission before accessing the camera.  If the
        // permission is not granted yet, request permission.
        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            cameraController.createCameraSource();
        } else {
            cameraController.requestCameraPermission();
        }

    }

    public void getJsonObject(String destination) {
        //HTTPリクエストを行う Queue を生成する
        RequestQueue mQueue = Volley.newRequestQueue(this);

        //JSON用URL
        String s = destination;

        //JSONでGET
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, s,
                        response -> {
                            try {
                                JSONObject json = new JSONObject(response.toString());
                                resultCode = json.getString("code");

                                if(resultCode.equals("0")){
                                    JSONObject data = json.getJSONObject("data");
                                    String iURL = data.getString("model_url");
                                    int id = data.getInt("id");
                                    int categoryId = data.getInt("category_id");

//                                        urlForImage = iURL;
//                                        ImageView iv = findViewById(R.id.tioCommodity);
//                                        System.out.println(urlForImage);
//                                        addUrlImage(urlForImage, iv);
                                    Model2D model = new Model2D(CameraActivity.this.getApplicationContext(), iURL);
                                    model.load();
                                    CameraController.GRAPHIC_TYPE type = null;
                                    switch (categoryId) {
                                        case 1: type = CameraController.GRAPHIC_TYPE.GLASSES;break;
                                        case 2: type = CameraController.GRAPHIC_TYPE.GLASSES;break;
                                        case 3: type = CameraController.GRAPHIC_TYPE.GLASSES;break;
                                        case 4: type = CameraController.GRAPHIC_TYPE.BLUSHER;break;
                                    }

                                    cameraController.showModel(type, model);

                                    checkFavorite(id, userID);

                                    //read color info
                                    SharedPreferences preferences;
                                    preferences = CameraActivity.this.getSharedPreferences("DATA", Context.MODE_PRIVATE);
                                    String color = preferences.getString("theme", "bluebutton");
                                    if(favorited){
                                        if (color.equals("bluebutton")) {
                                            ImageView v = findViewById(R.id.fab);
                                            v.setImageResource(R.drawable.try_on_page_favorite_button_blue);
                                        } else {
                                            ImageView v = findViewById(R.id.fab);
                                            v.setImageResource(R.drawable.try_on_page_favorite_button_pink);
                                        }
                                    } else {
                                        ImageView v = findViewById(R.id.fab);
                                        v.setImageResource(R.drawable.try_on_page_favorite_button_unselected);
                                    }

                                } else {
                                    msg = json.getString("msg");
                                    //User Feedback
                                    AlertDialog.Builder dl = new AlertDialog.Builder(CameraActivity.this);
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
                        },
                        error -> {
                            // TODO: Handle error here
                        }
                );
        mQueue.add(jsonObjectRequest);
    }

    public void getJsonObjectList(String destination) {
        //HTTPリクエストを行う Queue を生成する
        RequestQueue mQueue = Volley.newRequestQueue(this);

        //JSON用URL
        String s = destination;

        //JSONでGET
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, s,
                        response -> {
                            try {
                                JSONObject json = new JSONObject(response.toString());
                                resultCode = json.getString("code");

                                if(resultCode.equals("0")){
                                    // GridViewのインスタンスを生成
                                    GridView gridview = findViewById(R.id.browselist);

                                    JSONArray array = json.getJSONArray("data");
                                    int count = array.length();
                                    final JSONObject[] commodities = new JSONObject[count];

                                    if(commodityName.size() > 0) {
                                        commodityName.clear();
                                        brandName.clear();
                                        imageURLs.clear();
                                        imgList.clear();
                                        idList.clear();
                                    }
                                    for (int i=0; i<count; i++){
                                        commodities[i] = array.getJSONObject(i);
                                    }

                                    for (int i=0; i<count; i++){
                                        cName = commodities[i].getString("commodity_name");
                                        bName = commodities[i].getString("brand_name");
                                        iURL = commodities[i].getString("commodity_desc_img");
                                        cid = commodities[i].getInt("commodity_id");

                                        commodityName.add(cName);
                                        brandName.add(bName);
                                        imageURLs.add(iURL);
                                        idList.add(cid);
                                    }

                                    // for-each commodityNameをR.drawable.名前としてintに変換してarrayに登録
                                    for (String s1 : commodityName){
                                        int imageId = getResources().getIdentifier(
                                                s1,"drawable", getPackageName());
                                        imgList.add(imageId);
                                    }

                                    // BaseAdapter を継承したGridAdapterのインスタンスを生成
                                    // 子要素のレイアウトファイル grid_items.xml を
                                    // activity_main.xml に inflate するためにGridAdapterに引数として渡す
                                    GridAdapter adapter = new GridAdapter(CameraActivity.this,
                                            R.layout.grid_items_tio,
                                            imgList,
                                            brandName,
                                            commodityName,
                                            imageURLs
                                    );

                                    adapter.notifyDataSetChanged();

                                    // gridViewにadapterをセット
                                    gridview.setAdapter(adapter);
                                    gridview.smoothScrollToPosition(idList.indexOf(commodityID)+2);
                                    gridview.invalidateViews();

                                    gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                                                //User Feedback
//                                                AlertDialog.Builder dl = new AlertDialog.Builder(CameraActivity.this);
//                                                dl.setTitle("id: " + idList.get(position));
//                                                dl.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                                    public void onClick(DialogInterface dialog, int which) {
//                                                        dialog.dismiss();
//                                                    }
//                                                });
//                                                dl.show();
//
                                            url_json_commodity = "http://18.219.212.60:8080/tio_backend/commodity/detail?commodityId=" + idList.get(position);
                                            getJsonObject(url_json_commodity);
                                            commodityID = idList.get(position);
                                        }
                                    });

                                } else {
                                    msg = json.getString("msg");
                                    //User Feedback
                                    AlertDialog.Builder dl = new AlertDialog.Builder(CameraActivity.this);
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
                        },
                        error -> {
                            // TODO: Handle error here
                        }
                );
        mQueue.add(jsonObjectRequest);
    }

    // ネットワークアクセスするURLを設定する
    private void addUrlImage(String url, View v){
        ImageView img = v.findViewById(R.id.tioCommodity);
        Picasso.with(CameraActivity.this)
                .load(url)
                .into(img);
    }

    private void checkFavorite(int commodityID, int userID){
        //HTTPリクエストを行う Queue を生成する
        RequestQueue mQueue = Volley.newRequestQueue(this);

        //JSON用URL
        String s = "http://18.219.212.60:8080/tio_backend/commodity/checkIsFavorite?commodityId=" + commodityID + "&userId=" + userID;

        //JSONでGET
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, s,
                        response -> {
                            try {
                                JSONObject json = new JSONObject(response.toString());
                                resultCode = json.getString("code");

                                if(resultCode.equals("0")){
                                    JSONObject data = json.getJSONObject("data");
                                    favorited = data.getBoolean("favorited");

                                    //read color info
                                    SharedPreferences preferences;
                                    preferences = CameraActivity.this.getSharedPreferences("DATA", Context.MODE_PRIVATE);
                                    String color = preferences.getString("theme", "bluebutton");
                                    if(favorited){
                                        if (color.equals("bluebutton")) {
                                            ImageView v = findViewById(R.id.fab);
                                            v.setImageResource(R.drawable.try_on_page_favorite_button_blue);
                                        } else {
                                            ImageView v = findViewById(R.id.fab);
                                            v.setImageResource(R.drawable.try_on_page_favorite_button_pink);
                                        }
                                    } else {
                                        ImageView v = findViewById(R.id.fab);
                                        v.setImageResource(R.drawable.try_on_page_favorite_button_unselected);
                                    }

                                } else {
                                    msg = json.getString("msg");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        error -> {
                            // TODO: Handle error here
                        }
                );
        mQueue.add(jsonObjectRequest);
    }

    private void updateFavorite(int commodityID, int userID){
        //HTTPリクエストを行う Queue を生成する
        RequestQueue mQueue = Volley.newRequestQueue(this);

        //JSON用URL
        String s = "http://18.219.212.60:8080/tio_backend/commodity/checkIsFavorite?commodityId=" + commodityID + "&userId=" + userID;

        //JSONでGET
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, s,
                        response -> {
                            try {
                                JSONObject json = new JSONObject(response.toString());
                                resultCode = json.getString("code");

                                if(resultCode.equals("0")){
                                    JSONObject data = json.getJSONObject("data");
                                    favorited = data.getBoolean("favorited");

                                    //read color info
                                    SharedPreferences preferences;
                                    preferences = CameraActivity.this.getSharedPreferences("DATA", Context.MODE_PRIVATE);
                                    String color = preferences.getString("theme", "bluebutton");
                                    if(favorited){
                                        if (color.equals("bluebutton")) {
                                            ImageView v = findViewById(R.id.fab);
                                            v.setImageResource(R.drawable.try_on_page_favorite_button_blue);
                                        } else {
                                            ImageView v = findViewById(R.id.fab);
                                            v.setImageResource(R.drawable.try_on_page_favorite_button_pink);
                                        }
                                    } else {
                                        ImageView v = findViewById(R.id.fab);
                                        v.setImageResource(R.drawable.try_on_page_favorite_button_unselected);
                                    }

                                    changeFavorited(favorited);
                                } else {
                                    msg = json.getString("msg");
                                    //User Feedback
                                    AlertDialog.Builder dl = new AlertDialog.Builder(CameraActivity.this);
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
                        },
                        error -> {
                            // TODO: Handle error here
                        }
                );
        mQueue.add(jsonObjectRequest);
    }

    private void changeFavorited(boolean isFavorited){
        //If favorited
        if(isFavorited) {
            changeFavoritedURL = "http://18.219.212.60:8080/tio_backend/commodity/delFavorite?commodityId=" + commodityID + "&userId=" + userID;
        }
        //If not favorited
        else {
            changeFavoritedURL = "http://18.219.212.60:8080/tio_backend/commodity/addFavorite?commodityId=" + commodityID + "&userId=" + userID;
        }

        //HTTPリクエストを行う Queue を生成する
        RequestQueue mQueue = Volley.newRequestQueue(this);

        //JSONでGET
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, changeFavoritedURL,
                        response -> {
                            try {
                                JSONObject json = new JSONObject(response.toString());
                                resultCode = json.getString("code");
                                if(resultCode.equals("0")){
                                    JSONObject data = json.getJSONObject("data");
                                    res = data.getString("msg");
                                } else {
                                    res = json.getString("msg");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            //read color info
                            SharedPreferences preferences;
                            preferences = CameraActivity.this.getSharedPreferences("DATA", Context.MODE_PRIVATE);
                            String color = preferences.getString("theme", "bluebutton");
                            if(!favorited){
                                if (color.equals("bluebutton")) {
                                    ImageView v = findViewById(R.id.fab);
                                    v.setImageResource(R.drawable.try_on_page_favorite_button_blue);
                                } else {
                                    ImageView v = findViewById(R.id.fab);
                                    v.setImageResource(R.drawable.try_on_page_favorite_button_pink);
                                }
                            } else {
                                ImageView v = findViewById(R.id.fab);
                                v.setImageResource(R.drawable.try_on_page_favorite_button_unselected);
                            }

                            //User Feedback
                            AlertDialog.Builder dl = new AlertDialog.Builder(CameraActivity.this);
                            dl.setTitle(res);
                            dl.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            dl.show();
                        },
                        error -> {
                            // TODO: Handle error here
                        }
                );
        mQueue.add(jsonObjectRequest);
    }

    /**
     * Restarts the camera.
     */
    @Override
    protected void onResume() {
        super.onResume();
        cameraController.startCameraSource();
    }

    /**
     * Stops the camera.
     */
    @Override
    protected void onPause() {
        super.onPause();
        cameraController.pause();
    }

    /**
     * Releases the resources associated with the camera source, the associated detector, and the
     * rest of the processing pipeline.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraController.destroy();
    }
}
