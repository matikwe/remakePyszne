package com.example.remakepyszne.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.remakepyszne.R;
import com.example.remakepyszne.other.ShopCartAdapter;
import com.example.remakepyszne.sql.QueryHelper;
import com.example.remakepyszne.util.Addresses;
import com.example.remakepyszne.util.Restaurants;
import com.example.remakepyszne.util.ShopCart;
import com.example.remakepyszne.util.Users;

import java.sql.SQLException;
import java.util.ArrayList;

public class ShopCartActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private Users users;
    private Addresses addresses;
    private Restaurants restaurants;
    private ListView listViewShopCart;
    protected ArrayList<ShopCart> shopCartArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_cart);

        Intent intent = getIntent();
        users = intent.getParcelableExtra("currentUser");
        addresses = intent.getParcelableExtra("currentAddress");
        restaurants = intent.getParcelableExtra("currentRestaurant");

        listViewShopCart = (ListView) findViewById(R.id.listViewShopCart);
        String query = "SELECT * FROM [remakePyszne].[dbo].[ShopCart] WHERE userid=" + users.getId()
                + "AND restaurantid=" + restaurants.getRestaurantID();
        try {
            shopCartArrayList = new QueryHelper(query).tryLoginToDataBaseForShopCart();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        ShopCartAdapter shopCartAdapter = new ShopCartAdapter(this, shopCartArrayList);
        listViewShopCart.setAdapter(shopCartAdapter);
        listViewShopCart.setOnItemClickListener(this);
    }

    public void backToProduct(View view) {
        Intent intent = new Intent(this, ProductActivity.class);
        intent.putExtra("currentUser", users);
        intent.putExtra("currentAddress", addresses);
        intent.putExtra("currentRestaurant", restaurants);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}