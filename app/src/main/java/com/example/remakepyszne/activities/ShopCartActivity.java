package com.example.remakepyszne.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.remakepyszne.R;
import com.example.remakepyszne.util.Addresses;
import com.example.remakepyszne.util.Restaurants;
import com.example.remakepyszne.util.Users;

public class ShopCartActivity extends AppCompatActivity {

    private Users users;
    private Addresses addresses;
    private Restaurants restaurants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_cart);

        Intent intent = getIntent();
        users = intent.getParcelableExtra("currentUser");
        addresses = intent.getParcelableExtra("currentAddress");
        restaurants = intent.getParcelableExtra("currentRestaurant");
    }
}