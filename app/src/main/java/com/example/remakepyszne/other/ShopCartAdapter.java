package com.example.remakepyszne.other;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.remakepyszne.R;
import com.example.remakepyszne.util.ShopCart;

import java.util.ArrayList;

public class ShopCartAdapter extends ArrayAdapter<ShopCart> {
    public ShopCartAdapter(Context context, ArrayList<ShopCart> shopCartArrayList) {
        super(context, 0, shopCartArrayList);
    }

    @SuppressLint("ResourceType")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ShopCart shopCart = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_shopcart, parent, false);
        }
        TextView nameProduct = (TextView) convertView.findViewById(R.id.nameProduct);

        nameProduct.setText(String.valueOf(shopCart.getPrice() * shopCart.getQuantity()));
        return convertView;
    }
}
