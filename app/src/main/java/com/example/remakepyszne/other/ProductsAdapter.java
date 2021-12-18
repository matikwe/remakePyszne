package com.example.remakepyszne.other;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.remakepyszne.R;
import com.example.remakepyszne.util.Products;
import com.example.remakepyszne.util.Users;

import java.util.ArrayList;

public class ProductsAdapter extends ArrayAdapter<Products> {
    private Users users;

    public ProductsAdapter(Context context, ArrayList<Products> productsArrayAdapter, Users users) {
        super(context, 0, productsArrayAdapter);
        this.users = users;
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
        TextView nameCategory = (TextView) convertView.findViewById(R.id.nameCategory);
        TextView price = (TextView) convertView.findViewById(R.id.price);
        LinearLayout linearLayoutShopCart = (LinearLayout) convertView.findViewById(R.id.linearLayoutShopCart);

        Glide.with(convertView)
                .load(products.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iconProduct);

        nameProduct.setText(products.getNameProduct());
        nameCategory.setText(products.getCategory());
        price.setText(products.getPrice() + " zł");

        if (users.getRole().equals("restaurant manager"))
            linearLayoutShopCart.setVisibility(View.GONE);

        return convertView;
    }
}
