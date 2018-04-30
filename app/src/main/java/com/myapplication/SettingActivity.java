package com.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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

public class SettingActivity extends AppCompatActivity {
    private TextView textView;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mNicknameView;
    private View mProgressView;
    private View mLoginFormView;
    private SharedPreferences preferences;
    private int idToCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email_setting);
        mEmailView.setEnabled(false);
        mEmailView.setFocusable(false);
        //populateAutoComplete();

        mNicknameView = (EditText) findViewById(R.id.nickname_setting);

        Button confirmButton = (Button) findViewById(R.id.confirm_change);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptChangeNickname();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        TextView signupText = (TextView) findViewById(R.id.forSetting2);
        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forSetting2();
            }
        });

        //Read Login Info
        preferences = getSharedPreferences("DATA", Context.MODE_PRIVATE);
        idToCheck = preferences.getInt("id", -1);
        //If logged in
        if (idToCheck != -1) {
            mEmailView.setText(preferences.getString("account", "Tap to log in"));
            mNicknameView.setText(preferences.getString("nickname", ""));
        }

        textView = findViewById(R.id.textView5);
    }

    private void forSetting2() {
        Intent intent = new Intent(this, SettingActivity2.class);
        startActivity(intent);
    }


    private void attemptChangeNickname() {
        //デバッグ用
        /*
        AlertDialog.Builder dl = new AlertDialog.Builder(this);
        dl.setTitle("Test");
        dl.setMessage("email: " + mEmailView.getText().toString()
                + "\nnickname: " + mNicknameView.getText().toString());
        dl.setPositiveButton("OK", null); //ボタン
        dl.show();
        */

        //ログインボタン押下後に扱うテキストを指定（デバッグ用にHTTP Responseを表示させる）
        setContentView(R.layout.activity_setting);
        //textView = findViewById(R.id.textView5);

        //HTTPリクエストを行う Queue を生成する
        RequestQueue mQueue = Volley.newRequestQueue(this);

        //JSON用URL
        String url_json = "http://18.219.212.60:8080/tio_backend/user/changeNickname?" +
                "userId=" + idToCheck +
                "&newNickname=" + mNicknameView.getText().toString();

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
                                    JSONObject data = json.getJSONObject("data");
                                    String nickname = data.getString("nickname");
                                    String account = data.getString("account");
                                    Integer id = data.getInt("id");
                                    //textView.setText("code:" + resultCode + ", account:" + account + ", nickname:" + nickname + ", id:" + id);

                                    //Edit Login Info
                                    preferences = getSharedPreferences("DATA", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("nickname", nickname); //update the nickname
                                    editor.apply();

                                    //Display
                                    mEmailView.setText(preferences.getString("account", "Tap to log in"));
                                    mNicknameView.setText(preferences.getString("nickname", ""));

                                    //User Feedback
                                    AlertDialog.Builder dl = new AlertDialog.Builder(SettingActivity.this);
                                    dl.setTitle("Your nickname has updated!");
                                    dl.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            SettingActivity.this.finish();
                                        }
                                    });
                                    dl.show();
                                } else {
                                    String msg = json.getString("msg");
                                    //textView.setText("code:" + resultCode + ", msg:" + msg);

                                    //User Feedback
                                    AlertDialog.Builder dl = new AlertDialog.Builder(SettingActivity.this);
                                        dl.setTitle("Error");
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
}