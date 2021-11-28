package com.example.remakepyszne.other;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.remakepyszne.R;
import com.example.remakepyszne.util.Products;

import java.util.ArrayList;

public class ProductsAdapter extends ArrayAdapter<Products> {
    public ProductsAdapter(Context context, ArrayList<Products> productsArrayAdapter) {
        super(context, 0, productsArrayAdapter);
    }

    @SuppressLint("ResourceType")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Products products = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_product, parent, false);
        }

        ImageView iconProduct = (ImageView) convertView.findViewById(R.id.iconProduct);
        TextView nameProduct = (TextView) convertView.findViewById(R.id.nameProduct);

        iconProduct.setImageResource(R.drawable.pobyku);
        nameProduct.setText(products.getNameProduct());

        return convertView;
    }
}
