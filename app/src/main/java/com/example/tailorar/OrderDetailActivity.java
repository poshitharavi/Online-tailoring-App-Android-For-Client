package com.example.tailorar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import java.text.DateFormat;
import java.util.Date;

public class OrderDetailActivity extends AppCompatActivity {

    Order order;
    TextView styleNameTxt, orderIdTxt, orderDateTxt, orderStatusTxt, tailorTxt, measurementTxt, paymentStatusTxt;
    ImageView imageView;
    Button makeOrderBtn,orderCompletedBtn;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        mRequestQueue = Volley.newRequestQueue(this);
        order = new Order();
        order = (Order) getIntent().getSerializableExtra("orderObj");

        styleNameTxt = findViewById(R.id.orderDetailNameTxt);
        orderIdTxt = findViewById(R.id.orderDetailOrderIdTxt);
        orderDateTxt = findViewById(R.id.orderDetailOrderDateTxt);
        orderStatusTxt = findViewById(R.id.orderDetailStatusTxt);
        tailorTxt = findViewById(R.id.orderDetailTailorTxt);
        measurementTxt = findViewById(R.id.orderDetailMeasurementTxt);
        paymentStatusTxt = findViewById(R.id.orderDetailPaymentStatusTxt);
        imageView = findViewById(R.id.orderDetailImg);
        makeOrderBtn = findViewById(R.id.orderDetailMakePaymentBtn);
        orderCompletedBtn = findViewById(R.id.orderDetailMarkeCompleteBtn);

        makeOrderBtn.setEnabled(false);
        orderCompletedBtn.setEnabled(false);
        if ( (order.getStatus().equals("on Sew process") || order.getStatus().equals("ready to deliver") || order.getStatus().equals("delivered")) && order.getPaymentStatus().equals("Pending")){
            makeOrderBtn.setEnabled(true);
        }
        if ( order.getStatus().equals("delivered")){
            orderCompletedBtn.setEnabled(true);
        }


        Date date = new Date(String.valueOf(order.getOrderDate()));
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());

        String measurments = "Height : " + order.getMesurment().getHeight() + "cm \n"
                + "Chest : " + order.getMesurment().getChest() + "cm \n"
                + "Waist Width : " + order.getMesurment().getWaist() + "cm \n"
                + "Hip : " + order.getMesurment().getHip() + "cm \n"
                + "Leg Length : " + order.getMesurment().getLeg() + "cm \n"
                + "Shoulder Width : " + order.getMesurment().getShoulder() + "cm \n";


        styleNameTxt.setText(order.getStyle().getStyleName());
        orderIdTxt.setText(String.valueOf(order.getOrderId()));
        orderDateTxt.setText(dateFormat.format(date));
        orderStatusTxt.setText(order.getStatus());
        tailorTxt.setText(order.getTailor().getName());
        measurementTxt.setText(measurments);
        paymentStatusTxt.setText(order.getPaymentStatus());
        imageView.setImageResource(order.getStyle().getImageResource());

        makeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                intent.putExtra("orderID", order.getOrderId());
                startActivity(intent);
                finish();
            }
        });

        orderCompletedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject reqObj = new JSONObject();
                try {
                    reqObj.put("status", "completed");

                    Log.d("tag", reqObj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT, getResources().getString(R.string.api_url) + "order/status-update/" + String.valueOf(order.getOrderId()), reqObj, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("Data: ", jsonObject.toString());
                        Toast.makeText(getApplicationContext(), "Successfully updated", Toast.LENGTH_LONG).show();
                        orderStatusTxt.setText("completed");
                        orderCompletedBtn.setEnabled(false);
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
        });

    }
}
