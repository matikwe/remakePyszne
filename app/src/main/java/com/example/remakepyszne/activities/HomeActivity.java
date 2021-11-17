package com.example.remakepyszne.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.remakepyszne.R;
import com.example.remakepyszne.other.GetCurrentLocation;
import com.example.remakepyszne.sql.QueryHelper;
import com.example.remakepyszne.util.Addresses;
import com.example.remakepyszne.util.Users;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HomeActivity extends AppCompatActivity {
    private EditText street, number, city, zip;
    private boolean stateEditText = true;
    private Users users;
    private Addresses addresses;
    private static final String emptyEditText = "Uzupełnij dane adresowe";
    private static final String badFormatStreet = "Błedna nazwa ulicy";
    private static final String badFormatNumber = "Numer domu powinien być w formacie xxxxY";
    private static final String badFormatCity = "Błedna nazwa miejscowości";
    private static final String badFormatZip = "Kod pocztowy powinien być w formacie xx-xxx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        users = intent.getParcelableExtra("currentUser");
        if (users.gerRole().equals("user")) {
            setContentView(R.layout.activity_homeuser);
            street = (EditText) findViewById(R.id.editTextStreet);
            number = (EditText) findViewById(R.id.editTextNumber);
            city = (EditText) findViewById(R.id.editTextCity);
            zip = (EditText) findViewById(R.id.editTextZip);
        }
        /*
        else if(users.gerRole().equals("restaurant manager"))
        {
            setContentView(R.layout.activity_managerhome);
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
            bottomNavigationView.setSelectedItemId(R.id.name1);

            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @SuppressLint("NonConstantResourceId")
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.name1:
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            overridePendingTransition(0,0);
                            return true;

                        case R.id.name2:
                            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                            overridePendingTransition(0,0);
                            return true;

                        case R.id.name3:
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            overridePendingTransition(0,0);
                            return true;
                    }
                    return false;
                }
            });
        }

         */
    }

    public void getCurrentLocation(View view) {
        GetCurrentLocation getCurrentLocation = new GetCurrentLocation();
        getCurrentLocation.getGeoLocation(this, street, number, city, zip);
        stateEditText = false;
        EditTextNonEditable(stateEditText);
    }

    public void getAddressFromDataBase(View view) throws SQLException {
        String query = "SELECT * FROM [remakePyszne].[dbo].[Addresses] WHERE userid='" + users.getId() + "';";
        addresses = new QueryHelper(query).tryLoginToDataBaeForAddresses();

        street.setText(addresses.getStreet());
        number.setText(addresses.getNumber());
        city.setText(addresses.getCity());
        zip.setText(addresses.getZip());

        stateEditText = false;
        EditTextNonEditable(stateEditText);
    }

    public void addAddressToDataBase(View view) throws SQLException {
        if (!stateEditText)
            Toast.makeText(getApplicationContext(), "Pola odblokowane możesz wpisać adres", Toast.LENGTH_LONG).show();
        stateEditText = true;
        EditTextNonEditable(stateEditText);

        if (street.getText().toString().isEmpty() || number.getText().toString().isEmpty() ||
                city.getText().toString().isEmpty() || zip.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), emptyEditText, Toast.LENGTH_LONG).show();
        } else {
            boolean addToDataBase = true;
            if (!validation("^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$", street.getText().toString())) {
                Toast.makeText(getApplicationContext(), badFormatStreet, Toast.LENGTH_LONG).show();
                addToDataBase = false;
            }

            if (!validation("^[0-9]{1,4}+[a-zA-Z]{0,1}$", number.getText().toString())) {
                Toast.makeText(getApplicationContext(), badFormatNumber, Toast.LENGTH_LONG).show();
                Log.d("Error val number", "Error");
                addToDataBase = false;
            }

            if (!validation("^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$", city.getText().toString())) {
                Toast.makeText(getApplicationContext(), badFormatCity, Toast.LENGTH_LONG).show();
                addToDataBase = false;
            }

            if(!validation("^[0-9]{2}(?:-[0-9]{3})?$", zip.getText().toString())){
                Toast.makeText(getApplicationContext(), badFormatZip, Toast.LENGTH_LONG).show();
                addToDataBase = false;
            }

            if(addToDataBase){
                String query = "INSERT INTO Addresses (userid, street, number, city, zip_code) values ('" + users.getId() +
                        "','" + street.getText().toString() + "','" + number.getText().toString() + "','" + city.getText().toString() +
                        "','" + zip.getText().toString() + "');";
                insertIntoDatabase(query);
                //open new activity
            }
        }
    }

    boolean validation(String regex, String dataFromValid) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(dataFromValid);
        return matcher.matches();
    }

    void EditTextNonEditable(boolean state) {
        street.setEnabled(state);
        number.setEnabled(state);
        city.setEnabled(state);
        zip.setEnabled(state);
    }

    public void insertIntoDatabase(String query) throws SQLException {
        new QueryHelper(query).tryLoginToDataBaseForUsers();
    }
}