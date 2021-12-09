package com.example.remakepyszne.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.remakepyszne.R;
import com.example.remakepyszne.other.ShopCartAdapter;
import com.example.remakepyszne.sql.QueryHelper;
import com.example.remakepyszne.util.Addresses;
import com.example.remakepyszne.util.Restaurants;
import com.example.remakepyszne.util.ShopCart;
import com.example.remakepyszne.util.Users;

import java.sql.SQLException;
import java.util.ArrayList;

public class ShopCartActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private Users users;
    private Addresses addresses;
    private Restaurants restaurants;
    private ListView listViewShopCart;
    protected ArrayList<ShopCart> shopCartArrayList = new ArrayList<>();
    ShopCartAdapter shopCartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_cart);

        Intent intent = getIntent();
        users = intent.getParcelableExtra("currentUser");
        addresses = intent.getParcelableExtra("currentAddress");
        restaurants = intent.getParcelableExtra("currentRestaurant");

        listViewShopCart = (ListView) findViewById(R.id.listViewShopCart);

        try {
            shopCartArrayList = new QueryHelper(getQueryToShopCartList()).tryLoginToDataBaseForShopCart();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        shopCartAdapter = new ShopCartAdapter(this, shopCartArrayList);
        listViewShopCart.setAdapter(shopCartAdapter);
        listViewShopCart.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    String getQueryToShopCartList() {
        String query = "SELECT shopCartid, userid, ShopCart.productid, quantity, ShopCart.restaurantid, ShopCart.price, nameProduct, imageProduct " +
                "FROM [remakePyszne].[dbo].[ShopCart]" +
                "INNER JOIN [remakePyszne].[dbo].[Products] " +
                "ON [remakePyszne].[dbo].[ShopCart].productid = [remakePyszne].[dbo].[Products].productid" +
                " WHERE userid=" + users.getId() + " AND ShopCart.restaurantid=" + restaurants.getRestaurantID();
        return query;
    }

    public void backToProduct(View view) {
        openActivity(ProductActivity.class);
    }

    public void orderProduct(View view) {

    }

    public void refreshShopCart(View view) {
        openActivity(ShopCartActivity.class);
    }

    public void openActivity(Class<?> cls){
        Intent intent = new Intent(this, cls);
        intent.putExtra("currentUser", users);
        intent.putExtra("currentAddress", addresses);
        intent.putExtra("currentRestaurant", restaurants);
        startActivity(intent);
    }
}