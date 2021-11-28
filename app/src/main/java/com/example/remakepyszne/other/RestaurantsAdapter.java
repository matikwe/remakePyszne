package com.example.remakepyszne.other;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
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

        nameRestaurant.setText(restaurants.getNameRestaurant());
        type.setText(restaurants.getType());

        if (restaurants.getLogo() > 0) {
            String name = "pobyku";

            String t = "pobyku";
            //iconRestaurant.setImageResource(restaurants.getLogo());
            //iconRestaurant.setImageURI(Uri.parse("C:\\Users\\Mateusz\\AndroidStudioProjects\\remakePyszne\\app\\src\\main\\res\\drawable\\mcdonald.png"));
            //String src= "C:\\Users\\Mateusz\\AndroidStudioProjects\\remakePyszne\\app\\src\\main\\res\\drawable\\mcdonald.png";
            //Bitmap bmImg = BitmapFactory.decodeFile(src);
            // iconRestaurant.setImageBitmap(bmImg);
            iconRestaurant.setImageBitmap(BitmapFactory.decodeFile("../src/main/res/drawable/mcdonald.png"));
        } else
            iconRestaurant.setImageResource(R.drawable.test);

        String addressString = "ul. " + restaurants.getStreet() + " " + restaurants.getNumber() + "\n"
                + restaurants.getZip() + " " + restaurants.getCity();

        address.setText(addressString);

        return convertView;
    }

}
