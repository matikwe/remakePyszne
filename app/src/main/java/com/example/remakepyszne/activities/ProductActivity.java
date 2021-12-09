package com.example.remakepyszne.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.remakepyszne.R;
import com.example.remakepyszne.other.ProductsAdapter;
import com.example.remakepyszne.sql.QueryHelper;
import com.example.remakepyszne.util.Addresses;
import com.example.remakepyszne.util.Products;
import com.example.remakepyszne.util.Restaurants;
import com.example.remakepyszne.util.ShopCart;
import com.example.remakepyszne.util.Users;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class ProductActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private Users users;
    private Addresses addresses;
    private Restaurants restaurants;
    private ListView listViewProduct;
    private Button buttonShopCart;
    protected ArrayList<Products> productsArrayList = new ArrayList<>();
    ArrayList<ShopCart> shopCartArrayList = null;
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

        try {
            setUpAdapter();
            updateShopCartIcon();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    void setUpAdapter() throws SQLException {
        availableProduct.setText("Dostępne produkty z: " + restaurants.getNameRestaurant());

        productsArrayList = new QueryHelper(getQueryToListProducts()).tryLoginToDataBaseForProducts();

        ProductsAdapter productsAdapter = new ProductsAdapter(this, productsArrayList);
        listViewProduct.setAdapter(productsAdapter);
        listViewProduct.setOnItemClickListener(this);

        buttonShopCart = (Button) findViewById(R.id.buttonShopCart);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

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
        Intent intent = new Intent(this, ShopCartActivity.class);
        intent.putExtra("currentUser", users);
        intent.putExtra("currentAddress", addresses);
        intent.putExtra("currentRestaurant", restaurants);
        startActivity(intent);
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

    public void backToRestaurant(View view) {
        Intent intent = new Intent(this, RestaurantActivity.class);
        intent.putExtra("currentUser", users);
        intent.putExtra("currentAddress", addresses);
        intent.putExtra("currentRestaurant", restaurants);
        startActivity(intent);
    }
}