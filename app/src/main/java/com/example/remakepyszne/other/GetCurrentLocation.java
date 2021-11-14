package com.example.remakepyszne.other;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.remakepyszne.util.Addresses;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class GetCurrentLocation {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private List<Address> address;
    private List<Addresses> addresses = new LinkedList<>();
    private String stringAddresses;

    public void getGeoLocation(Activity activity, TextView textView) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        try {
                            Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
                            address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            stringAddresses = address.get(0).getAddressLine(0);
                            Log.d("Addresses: ", "City: " + address.get(0).getLocality() + " Address: " + address.get(0).getAddressLine(0));
                            textView.setText(stringAddresses);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }
}
