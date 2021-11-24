package com.example.remakepyszne.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.remakepyszne.R;
import com.example.remakepyszne.other.RestaurantsAdapter;
import com.example.remakepyszne.sql.QueryHelper;
import com.example.remakepyszne.util.Addresses;
import com.example.remakepyszne.util.Restaurants;
import com.example.remakepyszne.util.Users;

import java.sql.SQLException;
import java.util.ArrayList;

public class RestaurantActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private Users users;
    private Addresses addresses;
    private Restaurants restaurants;
    private ListView listViewRestaurant;
    protected ArrayList<Restaurants> restaurantsArrayList = new ArrayList<Restaurants>();
    private static String query = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        Intent intent = getIntent();
        users = intent.getParcelableExtra("currentUser");
        addresses = intent.getParcelableExtra("currentAddress");

        listViewRestaurant = (ListView) findViewById(R.id.listViewRestaurant);

        //dorobić szukanie po kategorii
        query = "SELECT * FROM [remakePyszne].[dbo].[Restaurants] INNER JOIN [remakePyszne].[dbo].[DeliveryCity] " +
                "ON [remakePyszne].[dbo].[Restaurants].restaurantid = [remakePyszne].[dbo].[DeliveryCity].restaurantid " +
                "WHERE DeliveryCity.city LIKE '" + addresses.getCity() + "'";

        setUpAdapter(query);
    }

    void setUpAdapter(String query){

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

        Log.d("Current position", " " + restaurantsArrayList.get(i).getNameRestaurant() + " poz:" + i);
    }
}