package com.example.remakepyszne.other;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.remakepyszne.R;
import com.example.remakepyszne.activities.RestaurantActivity;
import com.example.remakepyszne.util.Restaurants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class RestaurantsAdapter extends ArrayAdapter<Restaurants> {
    public RestaurantsAdapter(Context context, ArrayList<Restaurants> restaurantsArrayAdapter) {
        super(context, 0, restaurantsArrayAdapter);
    }

    @SuppressLint("ResourceType")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Restaurants restaurants = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_restaurant, parent, false);
        }

        TextView nameRestaurant = (TextView) convertView.findViewById(R.id.nameRestaurant);
        TextView type = (TextView) convertView.findViewById(R.id.type);
        ImageView iconRestaurant = (ImageView) convertView.findViewById(R.id.iconRestaurant);
        TextView address = (TextView) convertView.findViewById(R.id.address);
        TextView time = (TextView) convertView.findViewById(R.id.time);

        nameRestaurant.setText(restaurants.getNameRestaurant());
        type.setText(restaurants.getType());

        Glide.with(convertView)
                .load(restaurants.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iconRestaurant);

        String addressString = "ul. " + restaurants.getStreet() + " " + restaurants.getNumber() + "\n"
                + restaurants.getZip() + " " + restaurants.getCity();
        address.setText(addressString);

        String pattern = "HH:mm";

        @SuppressLint("SimpleDateFormat")
        DateFormat df = new SimpleDateFormat(pattern);

        String timeRest = df.format(restaurants.getOpeningHour()) + " - " + df.format(restaurants.getClosingHour());
        time.setText(timeRest);

        return convertView;
    }
}
