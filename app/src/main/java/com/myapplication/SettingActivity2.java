package com.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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

public class SettingActivity2 extends AppCompatActivity
{
    private TextView textView;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mNicknameView;
    private View mProgressView;
    private View mLoginFormView;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting2);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.old_password);
        //populateAutoComplete();

        mNicknameView = (EditText) findViewById(R.id.new_password);

        Button confirmButton = (Button) findViewById(R.id.confirm_change);
        confirmButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                attemptChangePassword();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        textView = findViewById(R.id.textView6);
    }

    private void attemptChangePassword()
    {
        RequestQueue mQueue = Volley.newRequestQueue(this);

        preferences = getSharedPreferences("DATA", Context.MODE_PRIVATE);
        int idToCheck = preferences.getInt("id",-1);

        String url_json = "http://18.219.212.60:8080/tio_backend/user/changePassword?" +
                "userId=" + idToCheck+
                "&oldPassword=" + mEmailView.getText().toString() +
                "&newPassword=" + mNicknameView.getText().toString();

        System.out.println(url_json);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url_json,
                        new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject response)
                            {
                                try
                                {
                                    JSONObject json = new JSONObject(response.toString());
                                    String resultCode = json.getString("code");

                                    if(resultCode.equals("0")){
                                        String msg = json.getString("data");

                                        //User Feedback
                                        AlertDialog.Builder dl = new AlertDialog.Builder(SettingActivity2.this);
                                        dl.setTitle("Your password has updated!");
                                        dl.setPositiveButton("OK", new DialogInterface.OnClickListener()
                                        {
                                            public void onClick(DialogInterface dialog, int which)
                                            {
                                                dialog.dismiss();
                                                SettingActivity2.this.finish();
                                            }
                                        });
                                        dl.show();
                                    }
                                    else {
                                        String msg = json.getString("msg");

                                        //User Feedback
                                        AlertDialog.Builder dl = new AlertDialog.Builder(SettingActivity2.this);
                                            dl.setTitle("Error");
                                            dl.setMessage(msg);
                                            dl.setPositiveButton("OK", new DialogInterface.OnClickListener()
                                            {
                                                public void onClick(DialogInterface dialog, int which) {
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
}
