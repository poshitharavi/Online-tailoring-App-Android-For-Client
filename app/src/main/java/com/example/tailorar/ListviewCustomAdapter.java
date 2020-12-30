package com.example.tailorar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListviewCustomAdapter extends BaseAdapter {


    List<Order> ordersList = new ArrayList<>();
    Context mContext;

    public ListviewCustomAdapter(List<Order> ordersList, Context mContext) {
        this.ordersList = ordersList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return ordersList.size();
    }

    @Override
    public Object getItem(int position) {
        return ordersList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_item, parent, false);
        }

        Order tempOrder = (Order) getItem(position);

        TextView orderIdTxt = convertView.findViewById(R.id.listViewOrderIdtxt);
        TextView orderDateTxt = convertView.findViewById(R.id.listViewDatetxt);

        Date date = new Date(String.valueOf(tempOrder.getOrderDate()));
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(mContext);


        orderIdTxt.setText("Order Id : "+String.valueOf(tempOrder.getOrderId()));
        orderDateTxt.setText(dateFormat.format(date));


        return convertView;
    }
}
