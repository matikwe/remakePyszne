package com.example.remakepyszne.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.remakepyszne.R;
import com.example.remakepyszne.sql.QueryHelper;
import com.example.remakepyszne.util.Products;
import com.example.remakepyszne.util.Restaurants;
import com.example.remakepyszne.util.Users;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditValueProductActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private Users users;
    private Restaurants restaurants;
    private Products products;
    private EditText editTextNameProduct, editTextURL, editTextPrice;
    private TextView textViewEditValueProduct;
    private BottomNavigationView bottomNavigationView, bottomNavBack;
    private Button addProductButtonToDataBase, editProductButtonToDataBase;
    private CheckBox visibleCheckBox;
    private boolean addToDataBase = false;
    private static final String emptyEditText = "Uzupełnij dane";
    private static final String badFormatNameProduct = "Błedna nazwa produktu";
    private static final String badFormatURL = "Błedna nazwa linku";
    private static final String badFormatPrice = "Błedny format ceny";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_value_product);

        loadContent();
    }

    private void loadContent() {
        Intent intent = getIntent();
        users = intent.getParcelableExtra("currentUser");
        restaurants = intent.getParcelableExtra("currentRestaurant");
        products = intent.getParcelableExtra("currentProduct");

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavProductEdit);
        bottomNavBack = (BottomNavigationView) findViewById(R.id.bottomNavBack);
        addProductButtonToDataBase = (Button) findViewById(R.id.addProductButtonToDataBase);
        editProductButtonToDataBase = (Button) findViewById(R.id.editProductButtonToDataBase);
        editTextNameProduct = (EditText) findViewById(R.id.editTextNameProduct);
        editTextURL = (EditText) findViewById(R.id.editTextURL);
        editTextPrice = (EditText) findViewById(R.id.editTextPrice);
        textViewEditValueProduct = (TextView) findViewById(R.id.textViewEditValueProduct);
        visibleCheckBox = (CheckBox) findViewById(R.id.visibleCheckBox);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.addProduct);
        editProductButtonToDataBase.setVisibility(View.GONE);

        if (products == null) {
            bottomNavigationView.setVisibility(View.GONE);
            bottomNavBack.setOnNavigationItemSelectedListener(this);
        } else {
            bottomNavBack.setVisibility(View.GONE);
        }
    }

    public boolean validationData() {

        if (editTextNameProduct.getText().toString().isEmpty() || editTextURL.getText().toString().isEmpty() || editTextPrice.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), emptyEditText, Toast.LENGTH_LONG).show();
        } else {
            addToDataBase = true;
            if (!validation("^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$", editTextNameProduct.getText().toString())) {
                Toast.makeText(getApplicationContext(), badFormatNameProduct, Toast.LENGTH_LONG).show();
                addToDataBase = false;
            }
            if (!validation("https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)", editTextURL.getText().toString())) {
                Toast.makeText(getApplicationContext(), badFormatURL, Toast.LENGTH_LONG).show();
                addToDataBase = false;
            }
            if (!validation("[0-9]+([.][0-9]{1,2})?", editTextPrice.getText().toString())) {
                Toast.makeText(getApplicationContext(), badFormatPrice, Toast.LENGTH_LONG).show();
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

    private void addProduct() {
        editProductButtonToDataBase.setVisibility(View.GONE);
        addProductButtonToDataBase.setVisibility(View.VISIBLE);
        textViewEditValueProduct.setText("Dodaj produkt");
        clearEditText();
    }

    private void editProduct() {
        addProductButtonToDataBase.setVisibility(View.GONE);
        editProductButtonToDataBase.setVisibility(View.VISIBLE);
        textViewEditValueProduct.setText("Edytuj produkt");
        addDataToEditText();
    }

    private void clearEditText() {
        editTextNameProduct.setText("");
        editTextURL.setText("");
        editTextPrice.setText("");
    }

    private void addDataToEditText() {
        editTextNameProduct.setText(products.getNameProduct());
        editTextURL.setText(products.getImage());
        //editTextPrice.setText(Float.toString(products.getPrice()));
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addProduct:
                addProduct();
                bottomNavigationView.getMenu().findItem(R.id.addProduct).setChecked(true);
                break;
            case R.id.editProduct:
                editProduct();
                bottomNavigationView.getMenu().findItem(R.id.editProduct).setChecked(true);
                break;
            case R.id.backToProduct:
                openActivity(ProductActivity.class);
                bottomNavigationView.getMenu().findItem(R.id.backToProduct).setChecked(true);
                break;
            case R.id.back:
                openActivity(ProductActivity.class);
                break;
        }
        return false;
    }

    public void openActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        intent.putExtra("currentUser", users);
        intent.putExtra("currentRestaurant", restaurants);
        startActivity(intent);
    }

    public void addProductToDataBase(View view) {
        if (validationData()) {
            String query = "INSERT INTO Products(restaurantid,nameProduct,imageProduct,price,visible)" +
                    "VALUES(" + restaurants.getRestaurantID() + ",'" + editTextNameProduct.getText().toString() +
                    "','" + editTextURL.getText().toString() + "'," + Double.parseDouble(editTextPrice.getText().toString()) + ",'" + visibleCheckBox.isChecked() + "');";
            new QueryHelper(query).tryConnectToDatabase();
            openActivity(ProductActivity.class);
        }
    }

    public void editProductToDataBase(View view) {
        if (validationData()) {
            String query = "UPDATE Products SET nameProduct = '" + editTextNameProduct.getText().toString() + "', imageProduct='" + editTextURL.getText().toString() + "', price=" + Double.parseDouble(editTextPrice.getText().toString()) + ", visible='" + visibleCheckBox.isChecked() + "' WHERE productid=" + products.getProductID() + ";";
            new QueryHelper(query).tryConnectToDatabase();
            openActivity(ProductActivity.class);
        }
    }
}