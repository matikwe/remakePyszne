package com.example.remakepyszne.other;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.remakepyszne.R;
import com.example.remakepyszne.util.Products;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ProductsAdapter extends ArrayAdapter<Products> {
    public ProductsAdapter(Context context, ArrayList<Products> productsArrayAdapter) {
        super(context, 0, productsArrayAdapter);
    }

    //List<Integer> total = new LinkedList<>();

    @SuppressLint("ResourceType")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Products products = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_product, parent, false);
        }

        ImageView iconProduct = (ImageView) convertView.findViewById(R.id.iconProduct);
        TextView nameProduct = (TextView) convertView.findViewById(R.id.nameProduct);
        TextView price = (TextView) convertView.findViewById(R.id.price);

        Glide.with(convertView)
                .load(products.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iconProduct);

        nameProduct.setText(products.getNameProduct());
        price.setText(products.getPrice() + " zł");

        return convertView;
    }
}
