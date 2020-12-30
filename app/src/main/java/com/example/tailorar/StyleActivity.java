package com.example.tailorar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class StyleActivity extends AppCompatActivity {

    private RequestQueue mRequestQueue;
    List<Style> styleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setSelectedItemId(R.id.styleHome);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.styleHome:
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
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

        mRequestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, getResources().getString(R.string.api_url) + "style/get-all", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("Data: ", response.toString());
                styleList = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {

                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Style style = new Style();
                        style.setStyleId(jsonObject.getInt("styleId"));
                        style.setDescription(jsonObject.getString("Description"));
                        style.setStyleName(jsonObject.getString("styleName"));

                        switch (i) {
                            case 0:
                                style.setImageResource(R.drawable.shirt1);
                                break;
                            case 1:
                                style.setImageResource(R.drawable.shirt2);
                                break;
                            case 2:
                                style.setImageResource(R.drawable.shirt3);
                                break;
                            case 3:
                                style.setImageResource(R.drawable.shirt4);
                                break;

                        }

                        styleList.add(style);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                RecyclerView recyclerView = findViewById(R.id.styleRecycleView);
                RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext(), styleList);
                recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
                recyclerView.setAdapter(recyclerViewAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ", error.getMessage());
                Toast.makeText(getApplicationContext(), "Error in network : " + error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

        mRequestQueue.add(req);
    }
}
