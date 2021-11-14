package com.example.remakepyszne.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.remakepyszne.R;
import com.example.remakepyszne.other.GetCurrentLocation;
import com.example.remakepyszne.util.Addresses;
import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.LinkedList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private List<Addresses> addresses = new LinkedList<>();
    private TextView testTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        testTV=(TextView) findViewById(R.id.testTV);
    }

    public void getCurrentLocation(View view) {
        GetCurrentLocation getCurrentLocation = new GetCurrentLocation();
        getCurrentLocation.getGeoLocation(this,testTV);
    }
}