package com.myapplication;

import android.app.AlertDialog;
import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email_setting);
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
    }

    private void forSetting2() {
        Intent intent = new Intent(this, SettingActivity2.class);
        startActivity(intent);
    }


    private void attemptChangeNickname() {
        //デバッグ用
        AlertDialog.Builder dl = new AlertDialog.Builder(this);
        dl.setTitle("Test");
        dl.setMessage("email: " + mEmailView.getText().toString()
                + "\nnickname: " + mNicknameView.getText().toString());
        dl.setPositiveButton("OK", null); //ボタン
        dl.show();

        //ログインボタン押下後に扱うテキストを指定（デバッグ用にHTTP Responseを表示させる）
        setContentView(R.layout.activity_setting);
        textView = findViewById(R.id.textView4);

        //HTTPリクエストを行う Queue を生成する
        RequestQueue mQueue = Volley.newRequestQueue(this);

        //JSON用URL
        String urljson = "http://128.237.133.0:3000/user/changeNickname?" +
                "account=" + mEmailView.getText().toString() +
                "&nickname=" + mNicknameView.getText().toString();

        //デバッグ用URL
        urljson = "http://" + mEmailView.getText().toString() + ":3000/user/changeNickname?";

        //JSONでPOST
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, urljson,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONObject json = new JSONObject(response.toString());
                                    JSONObject data = json.getJSONObject("data");
                                    String request = "\nEmail: " + data.getString("email")
                                            + "\nNickname: " + data.getString("nickname");
                                    textView.setText(request);
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