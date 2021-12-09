package com.example.remakepyszne.other;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.remakepyszne.R;
import com.example.remakepyszne.activities.ShopCartActivity;
import com.example.remakepyszne.sql.QueryHelper;
import com.example.remakepyszne.util.ShopCart;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        Button minus = (Button) convertView.findViewById(R.id.minus);
        Button plus = (Button) convertView.findViewById(R.id.plus);
        Button deleteItem = (Button) convertView.findViewById(R.id.deleteItem);
        ListView listViewShopCart = (ListView) convertView.findViewById(R.id.listViewShopCart);

        View finalConvertView = convertView;

        loadContent(convertView, shopCart);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopCart.setQuantity(shopCart.getQuantity() + 1);
                updateQuantity(shopCart);
                loadContent(finalConvertView, shopCart);
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shopCart.getQuantity() >= 1) {
                    shopCart.setQuantity(shopCart.getQuantity() - 1);
                    updateQuantity(shopCart);
                }
                if (shopCart.getQuantity() == 0) {
                    deleteProductFromShopCart(shopCart);
                }

                loadContent(finalConvertView, shopCart);
            }
        });

        deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteProductFromShopCart(shopCart);
                loadContent(finalConvertView, shopCart);
            }
        });

        return convertView;
    }

    void deleteProductFromShopCart(ShopCart shopCart) {
        String delete = "DELETE FROM [remakePyszne].[dbo].[ShopCart] WHERE userid=" + shopCart.getUserid()
                + " AND productid=" + shopCart.getProductID() + " AND restaurantid=" + shopCart.getRestaurantID();
        new QueryHelper(delete).tryConnectToDatabase();
    }

    void updateQuantity(ShopCart shopCart) {
        String update = "UPDATE [remakePyszne].[dbo].[ShopCart] SET quantity = " + (shopCart.getQuantity()) + " WHERE userid=" + shopCart.getUserid()
                + " AND productid=" + shopCart.getProductID() + " AND restaurantid=" + shopCart.getRestaurantID();
        new QueryHelper(update).tryConnectToDatabase();
    }

    void loadContent(View convertView, ShopCart shopCart) {
        TextView nameProduct = (TextView) convertView.findViewById(R.id.nameProduct);
        TextView price = (TextView) convertView.findViewById(R.id.price);
        TextView quantity = (TextView) convertView.findViewById(R.id.quantity);
        ImageView iconProduct = (ImageView) convertView.findViewById(R.id.iconProduct);

        float sum = (shopCart.getPrice() * shopCart.getQuantity());

        BigDecimal bd = BigDecimal.valueOf(sum);
        bd = bd.setScale(2, RoundingMode.HALF_UP);

        price.setText(String.valueOf(bd));
        nameProduct.setText(shopCart.getNameProduct());
        quantity.setText(String.valueOf(shopCart.getQuantity()));

        Glide.with(convertView)
                .load(shopCart.getImageProduct())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iconProduct);
    }
}

