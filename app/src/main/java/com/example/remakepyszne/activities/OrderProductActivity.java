package com.example.remakepyszne.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.remakepyszne.R;
import com.example.remakepyszne.other.OrdersAdapter;
import com.example.remakepyszne.sql.QueryHelper;
import com.example.remakepyszne.util.Addresses;
import com.example.remakepyszne.util.Orders;
import com.example.remakepyszne.util.Restaurants;
import com.example.remakepyszne.util.Users;

import java.sql.SQLException;
import java.util.ArrayList;

public class OrderProductActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private Users users;
    private Addresses addresses;
    private Restaurants restaurants;
    protected ArrayList<Orders> ordersArrayList;
    ListView listViewOrders;
    Button backToRestaurant;
    TextView title;
    String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_product);

        Intent intent = getIntent();
        users = intent.getParcelableExtra("currentUser");
        addresses = intent.getParcelableExtra("currentAddress");

        listViewOrders = (ListView) findViewById(R.id.listViewOrderProduct);
        title = (TextView) findViewById(R.id.titleOrder);
        backToRestaurant = (Button) findViewById(R.id.backToRestaurant);

        if (users.getRole().equals("user")) {
            title.setText("Historia zamówień");
            query = getQueryToDisplayHistoryOrders();
        } else if (users.getRole().equals("restaurant manager")) {
            title.setText("Zamówione produkty");
            //backToRestaurant.setVisibility(View.GONE);
            test(View.GONE);
            query = getQueryToDisplayOrder();
        } else if (users.getRole().equals("provider")) {
            title.setText("Aktualne dostawy");
            backToRestaurant.setVisibility(View.GONE);
            query = getQueryToDisplayProductDelivery();
        }


        try {
            ordersArrayList = new QueryHelper(query).tryLoginToDataBaseForOrders();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        OrdersAdapter ordersAdapter = new OrdersAdapter(this, ordersArrayList);
        listViewOrders.setAdapter(ordersAdapter);
        listViewOrders.setOnItemClickListener(this);
    }

    void test(int v) {
        backToRestaurant.setVisibility(v);
    }

    String getQueryToDisplayHistoryOrders() {
        return "SELECT * FROM [remakePyszne].[dbo].[Orders] WHERE userid=" + users.getId() + " ORDER BY [orderDate]DESC, [orderTime]DESC;";
    }

    String getQueryToDisplayOrder() {
        return "SELECT * FROM [remakePyszne].[dbo].[Orders] "/*WHERE restaurantid=" + restaurants.getRestaurantID() */ + " ORDER BY [orderDate]DESC, [orderTime]DESC;";
    }

    String getQueryToDisplayProductDelivery() {
        return "SELECT * FROM [remakePyszne].[dbo].[Orders] WHERE providerid='null' ORDER BY [orderDate]DESC, [orderTime]DESC;";
    }

    public void backToRestaurant(View view) {
        Intent intent = new Intent(this, RestaurantActivity.class);
        intent.putExtra("currentUser", users);
        intent.putExtra("currentAddress", addresses);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}