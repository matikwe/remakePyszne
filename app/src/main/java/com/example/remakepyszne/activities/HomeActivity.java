package com.example.remakepyszne.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.remakepyszne.R;
import com.example.remakepyszne.other.GetCurrentLocation;
import com.example.remakepyszne.util.Users;

public class HomeActivity extends AppCompatActivity {
    private EditText street, number, city, zip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Users users = intent.getParcelableExtra("currentUser");
        if(users.getLogin().equals("1")){
            setContentView(R.layout.activity_homeuser);
            street = (EditText) findViewById(R.id.editTextStreet);
            number = (EditText) findViewById(R.id.editTextNumber);
            city = (EditText) findViewById(R.id.editTextCity);
            zip = (EditText) findViewById(R.id.editTextZip);
        }

        else
            setContentView(R.layout.activity_managerhome);




    }

    public void getCurrentLocation(View view) {
        GetCurrentLocation getCurrentLocation = new GetCurrentLocation();
        getCurrentLocation.getGeoLocation(this, street, number, city, zip);
        EditTextNonEditable();
    }

    public void getAddressFromDataBase(View view) {

    }

    void EditTextNonEditable(){
        street.setEnabled(false);
        number.setEnabled(false);
        city.setEnabled(false);
        zip.setEnabled(false);
    }
}