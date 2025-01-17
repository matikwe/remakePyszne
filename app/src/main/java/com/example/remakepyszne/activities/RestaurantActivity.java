package com.example.remakepyszne.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.remakepyszne.R;
import com.example.remakepyszne.other.RestaurantsAdapter;
import com.example.remakepyszne.sql.QueryHelper;
import com.example.remakepyszne.util.Addresses;
import com.example.remakepyszne.util.Restaurants;
import com.example.remakepyszne.util.Users;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;

public class RestaurantActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, BottomNavigationView.OnNavigationItemSelectedListener {
    public Users users;
    private Addresses addresses;
    private ListView listViewRestaurant;
    private TextView TextViewAvailableProduct;
    private HorizontalScrollView categoryScrollView;
    private BottomNavigationView bottomNavigationView,bottomNavAdd1;
    protected ArrayList<Restaurants> restaurantsArrayList = new ArrayList<Restaurants>();
    static final String emptyListRestaurants = "W tej chwili żadna restauracja nie przyjmuje zmówień dla tej kategorii.\nSpróbuj ponownie później!";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        loadContent();

        listViewRestaurant = (ListView) findViewById(R.id.listViewRestaurant);
        setUpAdapter(null);
    }

    private void loadContent(){
        Intent intent = getIntent();
        users = intent.getParcelableExtra("currentUser");
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavAdd);
        bottomNavAdd1 = (BottomNavigationView)findViewById(R.id.historySoloBottomNavigationView);

        if (users.getRole().equals("user")) {
            addresses = intent.getParcelableExtra("currentAddress");
            Log.d("date", users.getLogin() + addresses.getCity());
            bottomNavigationView.setVisibility(View.GONE);
            bottomNavAdd1.setOnNavigationItemSelectedListener(this);
        } else if (users.getRole().equals("restaurant manager")) {
            TextViewAvailableProduct = (TextView) findViewById(R.id.TextViewAvailableProduct);
            categoryScrollView = (HorizontalScrollView) findViewById(R.id.categoryScrollView);
            TextViewAvailableProduct.setVisibility(View.GONE);
            categoryScrollView.setVisibility(View.GONE);
            bottomNavigationView.setOnNavigationItemSelectedListener(this);
            bottomNavAdd1.setVisibility(View.GONE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void setUpAdapter(String type) {

        String time = java.time.LocalTime.now(ZoneId.of("ECT")).toString();
        Log.d("Current time: ", time);

        if (type == null) {
            type = "";
        }

        try {
            restaurantsArrayList = new QueryHelper(getQueryToListRestaurants(time, type)).tryLoginToDataBaseForRestaurants();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        RestaurantsAdapter restaurantsAdapter = new RestaurantsAdapter(this, restaurantsArrayList);
        listViewRestaurant.setAdapter(restaurantsAdapter);
        listViewRestaurant.setOnItemClickListener(this);

        Log.d("liczba list: ", String.valueOf(restaurantsArrayList.size()));

        if (restaurantsArrayList.isEmpty()) {
            Toast.makeText(getApplicationContext(), emptyListRestaurants, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (users.getRole().equals("user")) {
            openActivity(restaurantsArrayList.get(i), ProductActivity.class);
        } else if (users.getRole().equals("restaurant manager")) {
            openActivity(restaurantsArrayList.get(i), EditValueRestaurantActivity.class);
        }
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

    public void openActivity(Restaurants restaurants, Class<?> cls) {
        Intent intent = new Intent(this, cls);
        intent.putExtra("currentUser", users);

        if (users.getRole().equals("user")) {
            intent.putExtra("currentAddress", addresses);
        }
        intent.putExtra("currentRestaurant", restaurants);
        startActivity(intent);
    }

    String getQueryToListRestaurants(String time, String type) {
        String query = "";
        if (users.getRole().equals("user")) {
            query = "SELECT [remakePyszne].[dbo].[Restaurants].restaurantid,nameRestaurant,type,street,number,[remakePyszne].[dbo].[Restaurants].city,zip_code,openingHour,closingHour,image,restaurantManagerid FROM [remakePyszne].[dbo].[Restaurants] INNER JOIN [remakePyszne].[dbo].[DeliveryCity] " +
                    "ON [remakePyszne].[dbo].[Restaurants].restaurantid = [remakePyszne].[dbo].[DeliveryCity].restaurantid " +
                    " INNER JOIN [remakePyszne].[dbo].[Types] ON [remakePyszne].[dbo].[Restaurants].typeid = [remakePyszne].[dbo].[Types].typeid"+
                    " WHERE DeliveryCity.city LIKE '" + addresses.getCity() + "' AND type LIKE '" + type + "%' AND" +
                    "(Restaurants.openingHour < Restaurants.closingHour AND" +
                    "'" + time + "' >= Restaurants.openingHour AND" +
                    "'" + time + "' <= Restaurants.closingHour) OR (" +
                    "Restaurants.openingHour > Restaurants.closingHour AND" +
                    "('" + time + "' >= Restaurants.openingHour OR " +
                    "'" + time + "' <= Restaurants.closingHour))";
        } else if (users.getRole().equals("restaurant manager")) {
            query = "SELECT restaurantid,nameRestaurant,type,street,number,city,zip_code,openingHour,closingHour,image,restaurantManagerid " +
                    "FROM [remakePyszne].[dbo].[Restaurants] INNER JOIN [remakePyszne].[dbo].[Types] ON [remakePyszne].[dbo].[Restaurants].typeid = [remakePyszne].[dbo].[Types].typeid" +
                    " WHERE restaurantManagerid=" + users.getId();
        }
        return query;
    }

    public void openOrderActivity(View view) {
        Intent intent = new Intent(this, OrderProductActivity.class);
        intent.putExtra("currentUser", users);
        intent.putExtra("currentAddress", addresses);
        startActivity(intent);
    }

    private void logout() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addObject:
                openActivity(null, EditValueRestaurantActivity.class);
                break;
            case R.id.historySolo:
                openActivity(null, OrderProductActivity.class);
                break;
            case R.id.logout:
                logout();
                break;
        }
        return false;
    }
}