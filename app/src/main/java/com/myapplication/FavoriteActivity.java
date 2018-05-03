package com.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

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

public class FavoriteActivity extends AppCompatActivity {

    private int userID, commodityID;
    private String url, resultCode, msg;

    private List<Integer> favoriteList = new ArrayList<>();
    private List<String> commodityName = new ArrayList<>();
    private List<String> brandName = new ArrayList<>();
    private List<String> imageURLs = new ArrayList<>();

    // Resource IDを格納するarray
    private List<Integer> imgList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        SharedPreferences preferences = getSharedPreferences("DATA", Context.MODE_PRIVATE);
        userID = preferences.getInt("id", -1);

        url = "http://18.219.212.60:8080/tio_backend/user/favoriteList?userId=" + userID;
        getJsonObjectList(url);
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
                                        GridView gridview = findViewById(R.id.favoriteGridView);

                                        JSONArray array = json.getJSONArray("data");
                                        int count = array.length();
                                        JSONObject[] commodities = new JSONObject[count];

                                        for (int i=0; i<count; i++){
                                            commodities[i] = array.getJSONObject(i);
                                        }

                                        String cName = "";
                                        String bName = "";
                                        String iURL = "";

                                        for (int i=0; i<count; i++){
                                            cName = commodities[i].getString("commodity_name");
                                            bName = commodities[i].getString("brand_name");
                                            iURL = commodities[i].getString("commodity_desc_img");
                                            commodityID = commodities[i].getInt("commodity_id");

                                            commodityName.add(cName);
                                            brandName.add(bName);
                                            imageURLs.add(iURL);
                                            favoriteList.add(commodityID);
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
                                        GridAdapterFavorite adapter = new GridAdapterFavorite(FavoriteActivity.this,
                                                R.layout.grid_items_favorite,
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
                                                Intent intent = new Intent(getApplication(), DetailActivity.class);

                                                intent.putExtra("commodityID", favoriteList.get(position));

                                                startActivity(intent);
                                            }
                                        });

                                    } else {
                                        msg = json.getString("msg");
                                        //User Feedback
                                        AlertDialog.Builder dl = new AlertDialog.Builder(FavoriteActivity.this);
                                        dl.setTitle("code:" + resultCode + ", msg:" + msg);
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
}
