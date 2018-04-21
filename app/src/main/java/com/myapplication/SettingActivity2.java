package com.myapplication;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SettingActivity2 extends AppCompatActivity {
    private TextView textView;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mNicknameView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting2);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.old_password);
        //populateAutoComplete();

        mNicknameView = (EditText) findViewById(R.id.new_password);

        Button confirmButton = (Button) findViewById(R.id.confirm_change);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptChangeNickname();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void attemptChangeNickname() {
        //デバッグ用
        /*
        AlertDialog.Builder dl = new AlertDialog.Builder(this);
        dl.setTitle("Test");
        dl.setMessage("old: " + mEmailView.getText().toString()
                + "\nnew: " + mNicknameView.getText().toString());
        dl.setPositiveButton("OK", null); //ボタン
        dl.show();
        */

        //ログインボタン押下後に扱うテキストを指定（デバッグ用にHTTP Responseを表示させる）
        setContentView(R.layout.activity_setting2);
        textView = findViewById(R.id.textView6);

        //HTTPリクエストを行う Queue を生成する
        RequestQueue mQueue = Volley.newRequestQueue(this);

        //JSON用URL
        String url_json = "http://128.237.185.143:3000/user/changePassword?" +
                "userId=" + "6" +
                "&oldPassword=" + mEmailView.getText().toString() +
                "&newPassword=" + mNicknameView.getText().toString();

        System.out.println(url_json);

        //JSONでPOST
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url_json,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONObject json = new JSONObject(response.toString());
                                    String resultCode = json.getString("code");

                                    if(resultCode.equals("0")){
                                        String msg = json.getString("data");
                                        textView.setText("code:" + resultCode + ", msg:" + msg);
                                    } else {
                                        String msg = json.getString("msg");
                                        textView.setText("code:" + resultCode + ", msg:" + msg);
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