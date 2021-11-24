package com.example.remakepyszne.other;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.remakepyszne.R;
import com.example.remakepyszne.util.Restaurants;

import java.util.ArrayList;

public class RestaurantsAdapter extends ArrayAdapter<Restaurants> {
    public RestaurantsAdapter(Context context, ArrayList<Restaurants> restaurantsArrayAdapter) {
        super(context, 0, restaurantsArrayAdapter);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Restaurants restaurants = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
        }

        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvHome = (TextView) convertView.findViewById(R.id.tvHome);
        // Populate the data into the template view using the data object
        tvName.setText(restaurants.getNameRestaurant());
        tvHome.setText(restaurants.getType());
        // Return the completed view to render on screen

        return convertView;
    }

}
