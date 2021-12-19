package com.example.remakepyszne.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.remakepyszne.R;
import com.example.remakepyszne.other.ProductsAdapter;
import com.example.remakepyszne.sql.QueryHelper;
import com.example.remakepyszne.util.Addresses;
import com.example.remakepyszne.util.Products;
import com.example.remakepyszne.util.Restaurants;
import com.example.remakepyszne.util.ShopCart;
import com.example.remakepyszne.util.Users;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class ProductActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, BottomNavigationView.OnNavigationItemSelectedListener {
    private Users users;
    private Addresses addresses;
    private Restaurants restaurants;
    private ListView listViewProduct;
    private Button buttonShopCart;
    private BottomNavigationView bottomNavigationView,backSoloBottomNavigationView;
    protected ArrayList<Products> productsArrayList = new ArrayList<>();
    ArrayList<ShopCart> shopCartArrayList = null;
    TextView availableProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Intent intent = getIntent();
        users = intent.getParcelableExtra("currentUser");
        restaurants = intent.getParcelableExtra("currentRestaurant");
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavProduct);
        backSoloBottomNavigationView = (BottomNavigationView) findViewById(R.id.backSoloBottomNavigationView);

        if (users.getRole().equals("user")) {
            addresses = intent.getParcelableExtra("currentAddress");
            Log.d("date", users.getLogin() + addresses.getCity() + restaurants.getNameRestaurant());
            try {
                setUpAdapter();
                updateShopCartIcon();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            bottomNavigationView.setVisibility(View.GONE);
            backSoloBottomNavigationView.setOnNavigationItemSelectedListener(this);
        } else if (users.getRole().equals("restaurant manager")) {
            try {
                setUpAdapter();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            buttonShopCart.setVisibility(View.GONE);
            availableProduct.setVisibility(View.GONE);
            bottomNavigationView.setOnNavigationItemSelectedListener(this);
            backSoloBottomNavigationView.setVisibility(View.GONE);
        }
    }

    @SuppressLint("SetTextI18n")
    void setUpAdapter() throws SQLException {
        productsArrayList = new QueryHelper(getQueryToListProducts()).tryLoginToDataBaseForProducts();

        listViewProduct = (ListView) findViewById(R.id.listViewProduct);
        availableProduct = (TextView) findViewById(R.id.TextViewAvailableProduct);
        buttonShopCart = (Button) findViewById(R.id.buttonShopCart);

        if (users.getRole().equals("user"))
            availableProduct.setText("Dostępne produkty z: " + restaurants.getNameRestaurant());

        ProductsAdapter productsAdapter = new ProductsAdapter(this, productsArrayList, users);
        Log.d("setUpAdapter: ", String.valueOf(productsArrayList.isEmpty()));
        listViewProduct.setAdapter(productsAdapter);
        listViewProduct.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (users.getRole().equals("user")) {
            try {
                shopCartArrayList = new QueryHelper(getQueryToListShopCart(i)).tryLoginToDataBaseForShopCart();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            if (Objects.requireNonNull(shopCartArrayList).isEmpty()) {
                String queryAddProductToShoCart = "INSERT INTO [remakePyszne].[dbo].[ShopCart] (userid,productid,quantity,restaurantid, price) " +
                        "VALUES (" + users.getId() + "," + productsArrayList.get(i).getProductID() + "," + 1 + "," + restaurants.getRestaurantID() + "," + productsArrayList.get(i).getPrice() + ");";

                new QueryHelper(queryAddProductToShoCart).tryConnectToDatabase();
            } else {
                String updateProduct = "UPDATE [remakePyszne].[dbo].[ShopCart] SET quantity = " + (shopCartArrayList.get(0).getQuantity() + 1) + " WHERE userid=" + users.getId()
                        + " AND productid=" + productsArrayList.get(i).getProductID() + " AND restaurantid=" + restaurants.getRestaurantID();
                new QueryHelper(updateProduct).tryConnectToDatabase();
            }
            try {
                updateShopCartIcon();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else if (users.getRole().equals("restaurant manager")) {
            openActivity(productsArrayList.get(i), EditValueProductActivity.class);
        }
    }

    String getQueryToListProducts() {
        String query = "";
        if (users.getRole().equals("user")) {
            query = "SELECT *  FROM [remakePyszne].[dbo].[Restaurants] " +
                    "INNER JOIN [remakePyszne].[dbo].[DeliveryCity] " +
                    "ON [remakePyszne].[dbo].[Restaurants].restaurantid = [remakePyszne].[dbo].[DeliveryCity].restaurantid " +
                    "INNER JOIN [remakePyszne].[dbo].[Products] " +
                    "ON [remakePyszne].[dbo].[Restaurants].restaurantid = [remakePyszne].[dbo].[Products].restaurantid " +
                    "WHERE DeliveryCity.city LIKE '" + addresses.getCity() + "' AND Restaurants.restaurantid = " + restaurants.getRestaurantID() + ";";
        } else if (users.getRole().equals("restaurant manager")) {
            query = "SELECT *  FROM [remakePyszne].[dbo].[Restaurants] " +
                    "INNER JOIN [remakePyszne].[dbo].[Products] " +
                    "ON [remakePyszne].[dbo].[Restaurants].restaurantid = [remakePyszne].[dbo].[Products].restaurantid " +
                    "WHERE Restaurants.restaurantid = " + restaurants.getRestaurantID() + ";";
        }
        return query;
    }

    String getQueryToListShopCart(int i) {
        String query = "SELECT shopCartid, userid, ShopCart.productid, quantity, ShopCart.restaurantid, ShopCart.price, nameProduct, imageProduct " +
                "FROM [remakePyszne].[dbo].[ShopCart]" +
                "INNER JOIN [remakePyszne].[dbo].[Products] " +
                "ON [remakePyszne].[dbo].[ShopCart].productid = [remakePyszne].[dbo].[Products].productid" +
                " WHERE userid=" + users.getId() + " AND ShopCart.productid="
                + productsArrayList.get(i).getProductID() + " AND ShopCart.restaurantid=" + restaurants.getRestaurantID();

        return query;
    }

    public void openShopCart(View view) {
        openActivity(null, ShopCartActivity.class);
    }

    @SuppressLint("SetTextI18n")
    void updateShopCartIcon() throws SQLException {
        String query = "SELECT sum(quantity) AS 'quantity' FROM [remakePyszne].[dbo].[ShopCart] WHERE userid=" +
                users.getId() + "AND restaurantid=" + restaurants.getRestaurantID();
        int quantity = new QueryHelper(query).getQuantity();
        if (quantity == 0)
            buttonShopCart.setText("KOSZYK(" + 0 + ")");
        else
            buttonShopCart.setText("KOSZYK(" + quantity + ")");
    }

    public void openActivity(Products products, Class<?> cls) {
        Intent intent = new Intent(this, cls);
        intent.putExtra("currentUser", users);

        if (users.getRole().equals("user")) {
            intent.putExtra("currentAddress", addresses);
        } else if (users.getRole().equals("restaurant manager")) {
            intent.putExtra("currentProduct", products);
        }
        intent.putExtra("currentRestaurant", restaurants);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addObject:
                openActivity(null, EditValueProductActivity.class);
                break;
            case R.id.backToRestFromProduct:
                openActivity(null, RestaurantActivity.class);
                break;
            case R.id.back:
                openActivity(null, RestaurantActivity.class);
                break;
        }
        return false;
    }
}