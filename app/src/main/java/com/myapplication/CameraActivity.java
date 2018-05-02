package com.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CameraActivity extends AppCompatActivity {

    private int commodityID;
    private String url_json_commodity, url_json_category, urlForImage, resultCode, msg, categoryName;

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

        moreInfoButton = findViewById(R.id.moreInfoButton);
        moreInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), DetailActivity.class);

                intent.putExtra("commodityID", commodityID);

                startActivity(intent);
            }
        });

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
                                        JSONObject data = json.getJSONObject("data");
                                        String iURL = data.getString("desc_img");
                                        String id = data.getString("id");

                                        urlForImage = iURL;
                                        ImageView iv = findViewById(R.id.tioCommodity);
                                        System.out.println(urlForImage);
                                        addUrlImage(urlForImage, iv);

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

    public void getJsonObjectList(String destination) {
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
                                        for (String s: commodityName){
                                            int imageId = getResources().getIdentifier(
                                                    s,"drawable", getPackageName());
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

    // ネットワークアクセスするURLを設定する
    private void addUrlImage(String url, View v){
        ImageView img = v.findViewById(R.id.tioCommodity);
        Picasso.with(CameraActivity.this)
                .load(url)
                .into(img);
    }
}
