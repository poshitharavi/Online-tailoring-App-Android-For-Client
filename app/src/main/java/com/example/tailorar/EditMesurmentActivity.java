package com.example.tailorar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class EditMesurmentActivity extends AppCompatActivity {

    public static final String KEY_EXTRA = "com.example.tailorar.KEY_MESURMENTS";
    Mesurment mesurment;
    Button submitBtn;
    EditText heightTxt,chestTxt,waistTxt,hipText, shoulderText,legTxt;
    TextView heightPredictTxt,chestPredictTxt,waistPredictTxt,hipPredictText, shoulderPredictText,legPredictTxt;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mesurment);

        mRequestQueue = Volley.newRequestQueue(this);

        mesurment = new Mesurment();
        submitBtn = findViewById(R.id.mesurmentSubmitBtn);
        heightTxt = findViewById(R.id.heightTxt);
        chestTxt = findViewById(R.id.chestTxt);
        waistTxt = findViewById(R.id.waistTxt);
        legTxt = findViewById(R.id.legTxt);
        hipText = findViewById(R.id.hipTxt);
        shoulderText = findViewById(R.id.shoulderTxt);

        heightPredictTxt = findViewById(R.id.heightPredictTxt);
        chestPredictTxt = findViewById(R.id.chestPredictTxt);
        waistPredictTxt = findViewById(R.id.waistPredictTxt);
        hipPredictText = findViewById(R.id.hipPredictTxt);
        shoulderPredictText = findViewById(R.id.shoulderPredictTxt);
        legPredictTxt = findViewById(R.id.legLengthPredictTxt);



        if (getIntent().hasExtra(KEY_EXTRA)) {

            try {
                String data = getIntent().getStringExtra(KEY_EXTRA);
                JSONObject jsonObject = new JSONObject(data);
                mesurment.setChest(jsonObject.getInt("chest"));
                mesurment.setHeight(jsonObject.getInt("height"));
                mesurment.setLeg(jsonObject.getInt("legLength"));
                mesurment.setWaist(jsonObject.getInt("wasitWidth"));
                mesurment.setHip(jsonObject.getInt("hip"));
                mesurment.setShoulder(jsonObject.getInt("shoulderWidth"));

                heightPredictTxt.setText(jsonObject.getString("heightPrediction"));
                chestPredictTxt.setText(jsonObject.getString("chestPrediction"));
                waistPredictTxt.setText(jsonObject.getString("wasitWidthPrediction"));
                shoulderPredictText.setText(jsonObject.getString("shoulderWidthPrediction"));
                hipPredictText.setText(jsonObject.getString("hipPrediction"));
                legPredictTxt.setText(jsonObject.getString("legLengthPrediction"));



            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + KEY_EXTRA);
        }

        //set mesurment data to edit texts
        chestTxt.setText(String.valueOf(mesurment.getChest()));
        heightTxt.setText(String.valueOf(mesurment.getHeight()));
        waistTxt.setText(String.valueOf(mesurment.getWaist()));
        legTxt.setText(String.valueOf(mesurment.getLeg()));
        hipText.setText(String.valueOf(mesurment.getHip()));
        shoulderText.setText(String.valueOf(mesurment.getShoulder()));

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences loginPreferences = getSharedPreferences("tailorUserDetails", MODE_PRIVATE);
                String userId = loginPreferences.getString("user_id", "0");


                JSONObject reqObj = new JSONObject();
                try {
                    reqObj.put("height", heightTxt.getText());
                    reqObj.put("chest", chestTxt.getText());
                    reqObj.put("waistWidth", waistTxt.getText());
                    reqObj.put("hip", hipText.getText());
                    reqObj.put("legLength", legTxt.getText());
                    reqObj.put("shoulderWidth", shoulderText.getText());
                    reqObj.put("userId",userId );

                    Log.d("tag", reqObj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, getResources().getString(R.string.api_url) + "mesurment/save", reqObj, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("Data: ", jsonObject.toString());
                        try {
                            if (!jsonObject.has("status")) {
                                Intent intent = new Intent(getApplicationContext(),StyleActivity.class);
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
            }
        });



    }
}
