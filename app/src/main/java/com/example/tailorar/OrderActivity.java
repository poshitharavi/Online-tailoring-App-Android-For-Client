package com.example.tailorar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OrderActivity extends AppCompatActivity {

    ListView orderListView;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        orderListView = findViewById(R.id.orderListView);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setSelectedItemId(R.id.orders);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.styleHome:
                        startActivity(new Intent(getApplicationContext(), StyleActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.orders:
                        return true;
                }
                return false;
            }
        });

        mRequestQueue = Volley.newRequestQueue(this);

        SharedPreferences loginPreferences = getSharedPreferences("tailorUserDetails", MODE_PRIVATE);
        String userId = loginPreferences.getString("user_id", "0");


        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, getResources().getString(R.string.api_url) + "order/user-status/" + userId+"/Pending", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("Data: ", response.toString());
                //load order list

                if (response.length() > 0) {
                    ArrayList<Order> ordersList = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {

                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            Order order = new Order();

                            order.setOrderId(jsonObject.getInt("orderId"));
                            order.setPaymentStatus(jsonObject.getString("paymentStatus"));
                            order.setStatus(jsonObject.getString("status"));

                            String dateStr = jsonObject.getString("orderDate");
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            Date orderDate = sdf.parse(dateStr);
                            order.setOrderDate(orderDate);

                            Mesurment mesurment = new Mesurment();
                            JSONObject mesumentJson = (JSONObject) jsonObject.get("mesurment");
                            mesurment.setChest(mesumentJson.getInt("chest"));
                            mesurment.setHeight(mesumentJson.getInt("height"));
                            mesurment.setLeg(mesumentJson.getInt("legLength"));
                            mesurment.setWaist(mesumentJson.getInt("waistWidth"));
                            mesurment.setHip(mesumentJson.getInt("hip"));
                            mesurment.setShoulder(mesumentJson.getInt("shoulderWidth"));
                            order.setMesurment(mesurment);

                            Style style = new Style();
                            JSONObject styleJson = (JSONObject) jsonObject.get("style");
                            style.setStyleId(styleJson.getInt("styleId"));
                            style.setDescription(styleJson.getString("Description"));
                            style.setStyleName(styleJson.getString("styleName"));

                            switch (style.getStyleId()) {
                                case 1:
                                    style.setImageResource(R.drawable.shirt1);
                                    break;
                                case 2:
                                    style.setImageResource(R.drawable.shirt2);
                                    break;
                                case 3:
                                    style.setImageResource(R.drawable.shirt3);
                                    break;
                                case 4:
                                    style.setImageResource(R.drawable.shirt4);
                                    break;

                            }
                            order.setStyle(style);


                            Tailor tailor = new Tailor();
                            JSONObject tailorJson = (JSONObject) jsonObject.get("tailor");
                            tailor.setEmail(tailorJson.getString("email"));
                            tailor.setName(tailorJson.getString("name"));
                            tailor.setTailorId(tailorJson.getInt("tailorId"));
                            order.setTailor(tailor);

                            User user = new User();
                            JSONObject UserJson = (JSONObject) jsonObject.get("tailor");
                            user.setEmail(UserJson.getString("email"));
                            user.setName(UserJson.getString("name"));
                            user.setUserId(UserJson.getInt("tailorId"));
                            order.setUser(user);


                            ordersList.add(order);

                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }

                    }

                    ListviewCustomAdapter listviewCustomAdapter = new ListviewCustomAdapter(ordersList, OrderActivity.this);
                    orderListView.setAdapter(listviewCustomAdapter);

                    orderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(view.getContext(), OrderDetailActivity.class);
                            intent.putExtra("orderObj", ordersList.get(position));
                            startActivity(intent);
                        }
                    });
                }else {
                    Toast.makeText(getApplicationContext(), "No orders", Toast.LENGTH_LONG).show();
                }
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
