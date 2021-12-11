package com.example.remakepyszne.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.remakepyszne.R;
import com.example.remakepyszne.other.ShopCartAdapter;
import com.example.remakepyszne.sql.QueryHelper;
import com.example.remakepyszne.util.Addresses;
import com.example.remakepyszne.util.Restaurants;
import com.example.remakepyszne.util.ShopCart;
import com.example.remakepyszne.util.Users;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ShopCartActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private Users users;
    private Addresses addresses;
    private Restaurants restaurants;
    private ListView listViewShopCart;
    protected ArrayList<ShopCart> shopCartArrayList = new ArrayList<>();
    ShopCartAdapter shopCartAdapter;
    final String emptyShopCart = "Twój koszyk jest pusty...";

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

        shopCartAdapter = new ShopCartAdapter(this, shopCartArrayList, users, addresses, restaurants);
        listViewShopCart.setAdapter(shopCartAdapter);
        listViewShopCart.setOnItemClickListener(this);
        Log.d("date", users.getLogin() + addresses.getCity() + restaurants.getNameRestaurant());
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void orderProduct(View view) throws SQLException {
        String query = "SELECT sum(quantity) AS quantity FROM [remakePyszne].[dbo].[ShopCart] WHERE userid=" + users.getId() + " AND ShopCart.restaurantid=" + restaurants.getRestaurantID();
        int quantity = new QueryHelper(query).getQuantity();

        if (quantity >= 1) {
            insertProductToOrder(shopCartArrayList.get(0));
            deleteShopCartFromUser(shopCartArrayList.get(0));
            openActivity(OrderProductActivity.class);
        } else {
            Toast.makeText(getApplicationContext(), emptyShopCart, Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void insertProductToOrder(ShopCart shopCart) {
        LocalDateTime time = LocalDateTime.now(ZoneId.of("ECT"));
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();

        String insert = "INSERT INTO [remakePyszne].[dbo].[Orders] (userid, restaurantid, addressid, description, orderTime, orderDate, state, totalPrice) VALUES("
                + shopCart.getUserid() + "," + shopCart.getRestaurantID() + "," + addresses.getAddressID() + ",'" + getAllShopCart()
                + "','" + timeFormatter.format(time) + "','" + dtf.format(now) + "','w realizacji'," + getTotalCost() + ")";
        new QueryHelper(insert).tryConnectToDatabase();
    }

    String getAllShopCart() {
        StringBuilder items = new StringBuilder();

        for (ShopCart shopCart : shopCartArrayList) {
            items.append(shopCart.getQuantity()).append(" x ").append(shopCart.getNameProduct()).append(" = ").append(shopCart.getQuantity() * shopCart.getPrice()).append(", ");
        }

        return items.toString();
    }

    BigDecimal getTotalCost() {
        float total = 0;

        for (ShopCart shopCart : shopCartArrayList) {
            total += (shopCart.getQuantity() * shopCart.getPrice());
        }
        BigDecimal bd = BigDecimal.valueOf(total);
        return bd.setScale(2, RoundingMode.HALF_UP);
    }

    void deleteShopCartFromUser(ShopCart shopCart) {
        String delete = "DELETE FROM [remakePyszne].[dbo].[ShopCart] WHERE userid=" + shopCart.getUserid() + " AND restaurantid=" + shopCart.getRestaurantID();
        new QueryHelper(delete).tryConnectToDatabase();
    }

    public void openActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        intent.putExtra("currentUser", users);
        intent.putExtra("currentAddress", addresses);
        intent.putExtra("currentRestaurant", restaurants);
        startActivity(intent);
    }
}