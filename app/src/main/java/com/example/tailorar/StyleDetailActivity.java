package com.example.tailorar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StyleDetailActivity extends AppCompatActivity {

    Button arBtn,orderBtn;
    TextView styleNameTxt,styleDes;
    ImageView styleImg;
    Spinner spinner;
    private RequestQueue mRequestQueue;
    List<Tailor> tailors;
    List<String> tailorsNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style_detail);

        arBtn = findViewById(R.id.styleDetailARbtn);
        orderBtn = findViewById(R.id.styleDetailOrderBtn);
        styleNameTxt = findViewById(R.id.styleDetilNameTxt);
        styleDes = findViewById(R.id.styleDetilDesxriptionTxt);
        styleImg = findViewById(R.id.styleDetailImg);
        spinner = findViewById(R.id.tailorSpinner);

        Intent intent = getIntent();
        int styleId = intent.getExtras().getInt("styleId");
        String title = intent.getExtras().getString("styleName");
        String des = intent.getExtras().getString("styleDescription");
        int img = intent.getExtras().getInt("styleImg");

        styleNameTxt.setText(title);
        styleDes.setText(des);
        styleImg.setImageResource(img);

        SharedPreferences loginPreferences = getSharedPreferences("tailorUserDetails", MODE_PRIVATE);
        String userId = loginPreferences.getString("user_id", "0");

        mRequestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, getResources().getString(R.string.api_url) + "tailor/get-all", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("Data: ", response.toString());
                tailors = new ArrayList<>();
                tailorsNames = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {

                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Tailor tailor = new Tailor();
                        tailor.setTailorId(jsonObject.getInt("tailorId"));
                        tailor.setName(jsonObject.getString("name"));

                        tailors.add(tailor);
                        tailorsNames.add(tailor.getName());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item,tailorsNames);
                spinner.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ", error.getMessage());
                Toast.makeText(getApplicationContext(), "Error in network : " + error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

        mRequestQueue.add(req);


        arBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject reqObj = new JSONObject();
                try {
                    reqObj.put("styleId",String.valueOf(styleId));
                    reqObj.put("tailorId", tailors.get(spinner.getSelectedItemPosition()).getTailorId());
                    reqObj.put("userId", userId);
                    reqObj.put("paymentStatus", "Pending");
                    reqObj.put("status", "Pending");

                    Log.d("tag", reqObj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, getResources().getString(R.string.api_url) + "order/save", reqObj, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("Data: ", jsonObject.toString());
                        startActivity(new Intent(getApplicationContext(),PlaceOrderActivity.class));
                        finish();
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
    }
}
