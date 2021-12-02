package com.example.remakepyszne.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.remakepyszne.R;
import com.example.remakepyszne.other.ProductsAdapter;
import com.example.remakepyszne.sql.QueryHelper;
import com.example.remakepyszne.util.Addresses;
import com.example.remakepyszne.util.Products;
import com.example.remakepyszne.util.Restaurants;
import com.example.remakepyszne.util.Users;

import java.sql.SQLException;
import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private Users users;
    private Addresses addresses;
    private Restaurants restaurants;
    private ListView listViewProduct;
    protected ArrayList<Products> productsArrayList = new ArrayList<>();
    TextView availableProduct;
    String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Intent intent = getIntent();
        users = intent.getParcelableExtra("currentUser");
        addresses = intent.getParcelableExtra("currentAddress");
        restaurants = intent.getParcelableExtra("currentRestaurant");
        listViewProduct = (ListView) findViewById(R.id.listViewProduct);
        availableProduct = (TextView) findViewById(R.id.TextViewAvailableProduct);

        Log.d("Current: ", restaurants.getNameRestaurant() + " " + addresses.getStreet() + " " + users.getLogin());

        setUpAdapter();
    }

    void setUpAdapter() {
        availableProduct.setText("Dostępne produkty z: " + restaurants.getNameRestaurant());

        try {
            productsArrayList = new QueryHelper(getQueryToListProducts()).tryLoginToDataBaseForProducts();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        ProductsAdapter productsAdapter = new ProductsAdapter(this, productsArrayList);
        listViewProduct.setAdapter(productsAdapter);
        listViewProduct.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d("Current position", " " + productsArrayList.get(i).getNameProduct() + " poz:" + i);
        int count = 1;
        String query = "INSERT INTO [remakePyszne].[dbo].[ShopCart] (userid,productid,quantity,restaurantid) " +
                "VALUES (" + users.getId() + "," + productsArrayList.get(i).getProductID() + "," + count + "," + restaurants.getRestaurantID() + ");";

        new QueryHelper(query).tryConnectToDatabase();

    }

    String getQueryToListProducts() {
        String query = "SELECT *  FROM [remakePyszne].[dbo].[Restaurants] " +
                "INNER JOIN [remakePyszne].[dbo].[DeliveryCity] " +
                "ON [remakePyszne].[dbo].[Restaurants].restaurantid = [remakePyszne].[dbo].[DeliveryCity].restaurantid " +
                "INNER JOIN [remakePyszne].[dbo].[Products] " +
                "ON [remakePyszne].[dbo].[Restaurants].restaurantid = [remakePyszne].[dbo].[Products].restaurantid " +
                "WHERE DeliveryCity.city LIKE '" + addresses.getCity() + "' AND Restaurants.restaurantid = " + restaurants.getRestaurantID() + ";";
        return query;
    }

    public void openShopCart(View view) {
        Intent intent = new Intent(this, ShopCartActivity.class);
        intent.putExtra("currentUser", users);
        intent.putExtra("currentAddress", addresses);
        intent.putExtra("currentRestaurant", restaurants);
        startActivity(intent);
    }
}