package com.example.remakepyszne.other;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
public class GetCurrentLocation {

    FusedLocationProviderClient fusedLocationProviderClient;
    private List<Address> address;
    private boolean emptyLocation = false;

    public void getGeoLocation(Activity activity, EditText street, EditText number, EditText city, EditText zip) {
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
                            Log.d("Addresses: ",address.get(0).getAddressLine(0));
                            street.setText(address.get(0).getThoroughfare());
                            number.setText(address.get(0).getFeatureName());
                            city.setText(address.get(0).getLocality());
                            zip.setText(address.get(0).getPostalCode());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else{
                        emptyLocation = true;
                    }
                }
            });
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }

    public boolean isEmptyLocation() {
        return emptyLocation;
    }
}