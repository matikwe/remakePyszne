package com.example.remakepyszne.other;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.remakepyszne.R;
import com.example.remakepyszne.util.Orders;

import java.util.ArrayList;

public class OrdersAdapter extends ArrayAdapter<Orders> {
    public OrdersAdapter(Context context, ArrayList<Orders> ordersArrayList) {
        super(context, 0, ordersArrayList);
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        Orders orders = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_order, parent, false);
        }

        TextView description = (TextView) convertView.findViewById(R.id.description);
        description.setText(orders.getDescription());

        return convertView;
    }
}
