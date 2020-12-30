package com.example.tailorar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private RequestQueue mRequestQueue;
    TextView nameTxt;
    Button updateBtn, logoutBtn;
    EditText heightTxt, chestTxt, waistTxt, hipText, shoulderText, legTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Mesurment mesurment = new Mesurment();

        nameTxt = findViewById(R.id.profileNametxt);
        updateBtn = findViewById(R.id.profileUpdateBtn);
        logoutBtn = findViewById(R.id.profileLogoutBtn);
        heightTxt = findViewById(R.id.profileHeightTxt);
        chestTxt = findViewById(R.id.profileChestTxt);
        waistTxt = findViewById(R.id.profileWaistTxt);
        legTxt = findViewById(R.id.profileLegTxt);
        hipText = findViewById(R.id.profileHipTxt);
        shoulderText = findViewById(R.id.profileShoulderTxt);



        SharedPreferences loginPreferences = getSharedPreferences("tailorUserDetails", MODE_PRIVATE);
        String userId = loginPreferences.getString("user_id", "0");
        String userName = loginPreferences.getString("user_name", "No Name");

        nameTxt.setText(userName);

        mRequestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, getResources().getString(R.string.api_url) + "mesurment/user/" + userId, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e("Data ", jsonObject.toString());
                try {
                    if (!jsonObject.has("status")) {

                        mesurment.setMesurmentId(jsonObject.getInt("mesurmentId"));


//                        //set mesurment data to edit texts
                        chestTxt.setText(String.valueOf(jsonObject.getDouble("chest")));
                        heightTxt.setText(String.valueOf(jsonObject.getDouble("height")));
                        waistTxt.setText(String.valueOf(jsonObject.getDouble("waistWidth")));
                        legTxt.setText(String.valueOf(jsonObject.getDouble("legLength")));
                        hipText.setText(String.valueOf(jsonObject.getDouble("hip")));
                        shoulderText.setText(String.valueOf(jsonObject.getDouble("shoulderWidth")));


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

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject reqObj = new JSONObject();
                try {
                    reqObj.put("height", heightTxt.getText());
                    reqObj.put("chest", chestTxt.getText());
                    reqObj.put("waistWidth", waistTxt.getText());
                    reqObj.put("hip", hipText.getText());
                    reqObj.put("legLength", legTxt.getText());
                    reqObj.put("shoulderWidth", shoulderText.getText());

                    Log.d("tag", reqObj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT, getResources().getString(R.string.api_url) + "mesurment/update/" + mesurment.getMesurmentId(), reqObj, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("Data: ", jsonObject.toString());
                        try {
                            if (!jsonObject.has("status")) {

                                Toast.makeText(getApplicationContext(), "Updated Successfully ", Toast.LENGTH_LONG).show();

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
            }
        });

logoutBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        SharedPreferences preferences =getSharedPreferences("tailorUserDetails",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(getApplicationContext(),LogingActivity.class);
        startActivity(intent);
        finish();
    }
});







        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setSelectedItemId(R.id.profile);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.styleHome:
                        Log.e("tag", "Home selecetd");
                        startActivity(new Intent(getApplicationContext(), StyleActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profile:
                        return true;
                    case R.id.orders:
                        startActivity(new Intent(getApplicationContext(), OrderActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

    }
}
