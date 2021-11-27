package com.example.remakepyszne.other;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_restaurant, parent, false);
        }

        TextView nameRestaurant = (TextView) convertView.findViewById(R.id.nameRestaurant);
        TextView type = (TextView) convertView.findViewById(R.id.type);
        ImageView iconRestaurant = (ImageView) convertView.findViewById(R.id.iconRestaurant);
        TextView address = (TextView) convertView.findViewById(R.id.address);

        nameRestaurant.setText(restaurants.getNameRestaurant());
        type.setText(restaurants.getType());
        iconRestaurant.setImageResource(R.drawable.test);
        String addressString = "ul. Goplany 54a  \n44-321 Marklowice";
        address.setText(addressString);

        return convertView;
    }

}
