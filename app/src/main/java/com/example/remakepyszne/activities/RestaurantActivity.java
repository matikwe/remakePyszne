package com.example.remakepyszne.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.remakepyszne.R;
import com.example.remakepyszne.util.Addresses;
import com.example.remakepyszne.util.Users;

public class RestaurantActivity extends AppCompatActivity {
    private Users users;
    private Addresses addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        Intent intent = getIntent();
        users = intent.getParcelableExtra("currentUser");
        addresses = intent.getParcelableExtra("currentAddress");
        Toast.makeText(getApplicationContext(), addresses.getCity() + " " + users.getLogin(), Toast.LENGTH_LONG).show();
    }


}