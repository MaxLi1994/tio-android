package com.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity {

    private int commodityID;
    private String url_json_commodity, urlForImage, resultCode, msg, categoryName;
    private String detailName, detailDesc, detailURL;

    private String cName = "";
    private String bName = "";
    private String iURL = "";
    private Integer cid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        commodityID = intent.getIntExtra("commodityID", 0);

        url_json_commodity = "http://18.219.212.60:8080/tio_backend/commodity/detail?commodityId=" + commodityID;
        getJsonObject(url_json_commodity);
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
                                        detailName = data.getString("name");
                                        detailDesc = data.getString("desc");
                                        detailURL = data.getString("shop_url");
                                        String id = data.getString("id");

                                        TextView tv1 = findViewById(R.id.detailName);
                                        tv1.setText(detailName);

                                        TextView tv2 = findViewById(R.id.detailDescription);
                                        tv2.setText(detailDesc);

                                        TextView tv3 = findViewById(R.id.detailURL);
                                        tv3.setText(detailURL);

                                        urlForImage = iURL;
                                        ImageView iv = findViewById(R.id.detailImage);
                                        System.out.println(urlForImage);
                                        addUrlImage(urlForImage, iv);

                                    } else {
                                        msg = json.getString("msg");
                                        //User Feedback
                                        AlertDialog.Builder dl = new AlertDialog.Builder(DetailActivity.this);
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
        ImageView img = v.findViewById(R.id.detailImage);
        Picasso.with(DetailActivity.this)
                .load(url)
                .into(img);
    }
}

