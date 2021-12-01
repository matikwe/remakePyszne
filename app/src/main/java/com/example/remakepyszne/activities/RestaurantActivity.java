package com.example.remakepyszne.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.remakepyszne.R;
import com.example.remakepyszne.other.RestaurantsAdapter;
import com.example.remakepyszne.sql.QueryHelper;
import com.example.remakepyszne.util.Addresses;
import com.example.remakepyszne.util.Restaurants;
import com.example.remakepyszne.util.Users;

import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;

public class RestaurantActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private Users users;
    private Addresses addresses;
    private Restaurants restaurants;
    private ListView listViewRestaurant;
    ImageView iconRestaurant;
    protected ArrayList<Restaurants> restaurantsArrayList = new ArrayList<Restaurants>();
    private static String query = null;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        Intent intent = getIntent();
        users = intent.getParcelableExtra("currentUser");
        addresses = intent.getParcelableExtra("currentAddress");

        listViewRestaurant = (ListView) findViewById(R.id.listViewRestaurant);
        iconRestaurant = (ImageView) findViewById(R.id.iconRestaurant);

        //int drawableResourceId = this.getResources().getIdentifier("pobyku", "drawable", this.getPackageName());
        //iconRestaurant.setImageResource(drawableResourceId);

        setUpAdapter(null);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void setUpAdapter(String type) {

        String time = java.time.LocalTime.now(ZoneId.of("ECT")).toString();
        Log.d("Current time: ", time);

        if (type == null) {
            type = "";
        }

        query = "SELECT * FROM [remakePyszne].[dbo].[Restaurants] INNER JOIN [remakePyszne].[dbo].[DeliveryCity] " +
                "ON [remakePyszne].[dbo].[Restaurants].restaurantid = [remakePyszne].[dbo].[DeliveryCity].restaurantid " +
                "WHERE DeliveryCity.city LIKE '" + addresses.getCity() + "' AND Restaurants.type LIKE '" + type + "%' AND" +
                "(Restaurants.openingHour < Restaurants.closingHour AND" +
                "'" + time + "' >= Restaurants.openingHour AND" +
                "'" + time + "' <= Restaurants.closingHour) OR (" +
                "Restaurants.openingHour > Restaurants.closingHour AND" +
                "('" + time + "' >= Restaurants.openingHour OR " +
                "'" + time + "' <= Restaurants.closingHour))";

        try {
            restaurantsArrayList = new QueryHelper(query).tryLoginToDataBaseForRestaurants();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        RestaurantsAdapter restaurantsAdapter = new RestaurantsAdapter(this, restaurantsArrayList);
        listViewRestaurant.setAdapter(restaurantsAdapter);
        listViewRestaurant.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        openActivity(restaurantsArrayList.get(i));
        Log.d("Current position", " " + restaurantsArrayList.get(i).getNameRestaurant() + " poz:" + i);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sortByPizza(View view) {
        setUpAdapter("pizza");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sortByKebab(View view) {
        setUpAdapter("kebab");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sortByIndyjska(View view) {
        setUpAdapter("indyjska");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sortByWloska(View view) {
        setUpAdapter("włoska");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sortByBurger(View view) {
        setUpAdapter("Burger");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sortByAll(View view) {
        setUpAdapter(null);
    }

    public void openActivity(Restaurants restaurants) {
        Intent intent = new Intent(this, ProductActivity.class);
        intent.putExtra("currentUser", users);
        intent.putExtra("currentAddress", addresses);
        intent.putExtra("currentRestaurant", restaurants);
        startActivity(intent);
    }
}