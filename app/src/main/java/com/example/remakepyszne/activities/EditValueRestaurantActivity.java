package com.example.remakepyszne.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.remakepyszne.R;
import com.example.remakepyszne.sql.QueryHelper;
import com.example.remakepyszne.util.Restaurants;
import com.example.remakepyszne.util.Users;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditValueRestaurantActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    LinearLayout add, dataDeliveryLinearLayout;
    private EditText nameRestaurant, typeRestaurant, street, number, city, zip, openingHour, closingHour, editTextcityToDelivery;
    Button addRestButtonToDataBase, editRestButtonToDataBase;
    private TextView textViewEditValueRestaurant;
    private BottomNavigationView bottomNavigationView,bottomNavBack;
    private boolean addToDataBase = false;
    private static final String emptyEditText = "Uzupełnij dane";
    private static final String badFormatStreet = "Błedna nazwa ulicy";
    private static final String badFormatNameRestaurant = "Błedna nazwa restauracji";
    private static final String badFormatType = "Błedna nazwa typu restauracji";
    private static final String badFormatNumber = "Numer lokalu powinien być w formacie xxxxY";
    private static final String badFormatCity = "Błedna nazwa miejscowości";
    private static final String badFormatZip = "Kod pocztowy powinien być w formacie xx-xxx";
    private static final String badOpeningHour = "Zły format godziny otwarcia xx:xx";
    private static final String badClosingHour = "Zły format godziny zamknięcia xx:xx";
    private static final String badPlaceDelivery = "Błędna nazwa miejscowości do dowozu";
    private Users users;
    private Restaurants restaurants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_value_restaurant);

        Intent intent = getIntent();
        users = intent.getParcelableExtra("currentUser");
        restaurants = intent.getParcelableExtra("currentRestaurant");

        add = (LinearLayout) findViewById(R.id.dataRestLinearLayout);
        dataDeliveryLinearLayout = (LinearLayout) findViewById(R.id.dataDeliveryLinearLayout);

        nameRestaurant = (EditText) findViewById(R.id.editTextNameRestaurant);
        typeRestaurant = (EditText) findViewById(R.id.editTextTypeRestaurant);
        street = (EditText) findViewById(R.id.editTextStreet);
        number = (EditText) findViewById(R.id.editTextNumber);
        city = (EditText) findViewById(R.id.editTextCity);
        zip = (EditText) findViewById(R.id.editTextZip);

        textViewEditValueRestaurant = (TextView) findViewById(R.id.textViewEditValueRestaurant);

        openingHour = (EditText) findViewById(R.id.editTextOpeningHour);
        closingHour = (EditText) findViewById(R.id.editTextClosingHour);
        editTextcityToDelivery = (EditText) findViewById(R.id.editTextcityToDelivery);

        addRestButtonToDataBase = (Button) findViewById(R.id.addRestButtonToDataBase);
        editRestButtonToDataBase = (Button) findViewById(R.id.editRestButtonToDataBase);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavRestEdit);
        bottomNavBack = (BottomNavigationView) findViewById(R.id.bottomNavBack);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.addRestaurant);

        if(restaurants == null){
            bottomNavigationView.setVisibility(View.GONE);
            bottomNavBack.setOnNavigationItemSelectedListener(this);
        }else{
            bottomNavBack.setVisibility(View.GONE);
        }

        addRestaurant();
        dataDeliveryLinearLayout.setVisibility(View.GONE);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addRestaurant:
                addRestaurant();
                bottomNavigationView.getMenu().findItem(R.id.addRestaurant).setChecked(true);
                break;
            case R.id.editRestaurant:
                editRestaurant();
                bottomNavigationView.getMenu().findItem(R.id.editRestaurant).setChecked(true);
                break;
            case R.id.addDelivery:
                addDelivery();
                bottomNavigationView.getMenu().findItem(R.id.addDelivery).setChecked(true);
                break;
            case R.id.displayProduct:
                openActivity(ProductActivity.class);
                break;
            case R.id.backToRest:
                openActivity(RestaurantActivity.class);
                break;
            case R.id.back:
                openActivity(RestaurantActivity.class);
        }
        return false;
    }

    private void addRestaurant() {
        add.setVisibility(View.VISIBLE);
        dataDeliveryLinearLayout.setVisibility(View.GONE);
        editRestButtonToDataBase.setVisibility(View.GONE);
        addRestButtonToDataBase.setVisibility(View.VISIBLE);
        textViewEditValueRestaurant.setText("Dodaj restaurację");
        deleteDataFromEditText();
    }

    private void editRestaurant() {
        add.setVisibility(View.VISIBLE);
        dataDeliveryLinearLayout.setVisibility(View.GONE);
        addRestButtonToDataBase.setVisibility(View.GONE);
        editRestButtonToDataBase.setVisibility(View.VISIBLE);
        textViewEditValueRestaurant.setText("Edytuj restaurację");
        addDataToEditText();
    }

    private void addDelivery() {
        add.setVisibility(View.GONE);
        dataDeliveryLinearLayout.setVisibility(View.VISIBLE);
        editTextcityToDelivery.setVisibility(View.VISIBLE);
        textViewEditValueRestaurant.setText("Dodaj rejon dowozu");
    }

    public void addDeliveryDataToDataBase(View view) {
        if (!validation("^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$", editTextcityToDelivery.getText().toString())) {
            Toast.makeText(getApplicationContext(), badPlaceDelivery, Toast.LENGTH_LONG).show();
        } else {
            String query = "INSERT INTO DeliveryCity (restaurantid, city) VALUES(" + restaurants.getRestaurantID() + ", '" + editTextcityToDelivery.getText().toString() + "');";
            new QueryHelper(query).tryConnectToDatabase();
            openActivity(RestaurantActivity.class);
        }
    }

    private void addDataToEditText() {
        nameRestaurant.setText(restaurants.getNameRestaurant());
        typeRestaurant.setText(restaurants.getType());
        street.setText(restaurants.getStreet());
        number.setText(restaurants.getNumber());
        city.setText(restaurants.getCity());
        zip.setText(restaurants.getZip());
        //String pattern = "HH:mm";
        // @SuppressLint("SimpleDateFormat")
        //  DateFormat df = new SimpleDateFormat(pattern);
        //openingHour.setText(df.format(String.valueOf(restaurants.getOpeningHour())));
        //closingHour.setText(df.format(restaurants.getClosingHour()));
    }

    private void deleteDataFromEditText() {
        nameRestaurant.setText("");
        typeRestaurant.setText("");
        street.setText("");
        number.setText("");
        city.setText("");
        zip.setText("");
        openingHour.setText("");
        closingHour.setText("");
    }

    public void addRestaurantToDataBase(View view) {
        if (validationData()) {
            new QueryHelper(getQueryToInsetRestaurant()).tryConnectToDatabase();
            openActivity(RestaurantActivity.class);
        }
    }

    public void editRestaurantToDataBase(View view) {
        if (validationData()) {
            new QueryHelper(getQueryToUpdateRestaurant()).tryConnectToDatabase();
            openActivity(RestaurantActivity.class);
        }
    }

    private String getQueryToUpdateRestaurant() {
        String query = "UPDATE Restaurants SET nameRestaurant = '" + nameRestaurant.getText().toString() +
                "', type = '" + typeRestaurant.getText().toString() + "', street='" + street.getText().toString() +
                "', number='" + number.getText().toString() + "', city='" + city.getText().toString() + "', zip_code='" + zip.getText().toString() +
                "', openingHour='" + openingHour.getText().toString() + "', closingHour='" + closingHour.getText().toString() +
                "' WHERE restaurantid=" + restaurants.getRestaurantID();

        return query;
    }

    private String getQueryToInsetRestaurant() {
        String query = "INSERT INTO Restaurants (nameRestaurant, type, street, number, city, zip_code, openingHour, closingHour, image, restaurantManagerid)" +
                " VALUES ('" + nameRestaurant.getText().toString() + "','" + typeRestaurant.getText().toString() + "','" + street.getText().toString() + "','" + number.getText().toString() + "','" + city.getText().toString() +
                "','" + zip.getText().toString() + "','" + openingHour.getText().toString() + "','" + closingHour.getText().toString() + "','" + nameRestaurant.getText().toString() + "','" + users.getId() + "');";

        return query;
    }

    public boolean validationData() {
        if (nameRestaurant.getText().toString().isEmpty() || typeRestaurant.getText().toString().isEmpty() || street.getText().toString().isEmpty() ||
                number.getText().toString().isEmpty() || city.getText().toString().isEmpty() || zip.getText().toString().isEmpty() ||
                openingHour.getText().toString().isEmpty() || closingHour.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), emptyEditText, Toast.LENGTH_LONG).show();
        } else {
            addToDataBase = true;

            if (!validation("^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$", nameRestaurant.getText().toString())) {
                Toast.makeText(getApplicationContext(), badFormatNameRestaurant, Toast.LENGTH_LONG).show();
                addToDataBase = false;
            }

            if (!validation("^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$", typeRestaurant.getText().toString())) {
                Toast.makeText(getApplicationContext(), badFormatType, Toast.LENGTH_LONG).show();
                addToDataBase = false;
            }

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

            if (!validation("^[0-9]{2}(?:-[0-9]{3})?$", zip.getText().toString())) {
                Toast.makeText(getApplicationContext(), badFormatZip, Toast.LENGTH_LONG).show();
                addToDataBase = false;
            }

            if (!validation("([0-2][0-3]|[0-1][0-9]):([0-5][0-9]{1})", openingHour.getText().toString())) {
                Toast.makeText(getApplicationContext(), badOpeningHour, Toast.LENGTH_LONG).show();
                addToDataBase = false;
            }

            if (!validation("([0-2][0-3]|[0-1][0-9]):([0-5][0-9]{1})", closingHour.getText().toString())) {
                Toast.makeText(getApplicationContext(), badClosingHour, Toast.LENGTH_LONG).show();
                addToDataBase = false;
            }
        }
        return addToDataBase;
    }

    boolean validation(String regex, String dataFromValid) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(dataFromValid);
        return matcher.matches();
    }

    public void openActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        intent.putExtra("currentUser", users);
        intent.putExtra("currentRestaurant", restaurants);
        startActivity(intent);
    }

    public void openOrderActivity(View view) {
        openActivity(OrderProductActivity.class);
    }
}