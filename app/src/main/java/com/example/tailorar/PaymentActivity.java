package com.example.tailorar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.checkout.android_sdk.PaymentForm;
import com.checkout.android_sdk.Response.CardTokenisationResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentActivity extends AppCompatActivity {
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        mRequestQueue = Volley.newRequestQueue(this);
        PaymentForm mPaymentForm; // include the payment form

        int orderId = (int) getIntent().getSerializableExtra("orderID");
        JSONObject reqObj = new JSONObject();
        try {
            reqObj.put("paymentStatus", "completed");

            Log.d("tag", reqObj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT, getResources().getString(R.string.api_url) + "order/payment-status-update/" + String.valueOf(orderId), reqObj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.d("Data: ", jsonObject.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("Error: ", volleyError.toString());
                Toast.makeText(getApplicationContext(), "Error in network : " + volleyError.toString(), Toast.LENGTH_LONG).show();
            }
        });

        mRequestQueue.add(req);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), OrderActivity.class));
        finish();
    }
}
