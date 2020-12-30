package com.example.tailorar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LogingActivity extends AppCompatActivity {

    Button loginBtn, registerBtn;
    EditText emailTxt, passwordTxt;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loging);

        loginBtn = findViewById(R.id.loginLoginBtn);
        registerBtn = findViewById(R.id.loginRegisterBtn);
        emailTxt = findViewById(R.id.loginEmailTxt);
        passwordTxt = findViewById(R.id.loginPasswordTxt);
        mRequestQueue = Volley.newRequestQueue(this);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailTxt.getText().toString().length() > 0 && passwordTxt.getText().toString().length() > 0) {

                    JSONObject reqObj = new JSONObject();
                    try {
                        reqObj.put("email", emailTxt.getText());
                        reqObj.put("password", passwordTxt.getText());
                        Log.d("tag", reqObj.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, getResources().getString(R.string.api_url) + "user/auth-user", reqObj, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            Log.d("Data: ", jsonObject.toString());
                            try {
                                if (!jsonObject.has("status")) {
                                    SharedPreferences sharedPreferences = getSharedPreferences("tailorUserDetails", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();

                                    editor.putString("user_id", jsonObject.getString("userId"));
                                    editor.putString("user_name", jsonObject.getString("name"));
                                    editor.putString("user_email", jsonObject.getString("email"));
                                    editor.commit();

                                    Intent intent = new Intent(getApplicationContext(), StyleActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Error : " + jsonObject.getString("error"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Log.e("Error: ", volleyError.getMessage());
                            Toast.makeText(getApplicationContext(), "Error in network : " + volleyError.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                    mRequestQueue.add(req);
                } else {
                    Toast.makeText(getApplicationContext(), "Enter email and password", Toast.LENGTH_LONG).show();
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
