package com.example.remakepyszne.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.remakepyszne.R;
import com.example.remakepyszne.other.OrdersAdapter;
import com.example.remakepyszne.sql.QueryHelper;
import com.example.remakepyszne.util.Addresses;
import com.example.remakepyszne.util.Orders;
import com.example.remakepyszne.util.Restaurants;
import com.example.remakepyszne.util.Users;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.SQLException;
import java.util.ArrayList;

public class OrderProductActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, BottomNavigationView.OnNavigationItemSelectedListener {
    private Users users;
    private Addresses addresses;
    private Restaurants restaurants;
    protected ArrayList<Orders> ordersArrayList;
    BottomNavigationView orderBottomNavigationView, backSoloBottomNavigationView;
    ListView listViewOrders;
    Button backToRestaurant;
    TextView title;
    String query, nextState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_product);

        Intent intent = getIntent();
        users = intent.getParcelableExtra("currentUser");


        listViewOrders = (ListView) findViewById(R.id.listViewOrderProduct);
        title = (TextView) findViewById(R.id.titleOrder);
        orderBottomNavigationView = (BottomNavigationView) findViewById(R.id.orderBottomNavigationView);
        backSoloBottomNavigationView = (BottomNavigationView) findViewById(R.id.backSoloBottomNavigationView);

        if (users.getRole().equals("user")) {
            title.setText("Historia zamówień");
            addresses = intent.getParcelableExtra("currentAddress");
            query = getQueryToDisplayHistoryOrders();
            backSoloBottomNavigationView.setOnNavigationItemSelectedListener(this);
            orderBottomNavigationView.setVisibility(View.GONE);
        } else if (users.getRole().equals("restaurant manager")) {
            title.setText("Zamówione produkty");
            restaurants = intent.getParcelableExtra("currentRestaurant");
            query = getQueryToDisplayOrder();
            orderBottomNavigationView.setOnNavigationItemSelectedListener(this);
            orderBottomNavigationView.setSelectedItemId(R.id.accept);
            backSoloBottomNavigationView.setVisibility(View.GONE);
        } else if (users.getRole().equals("provider")) {
            title.setText("Aktualne dostawy");
            backToRestaurant.setVisibility(View.GONE);
            query = getQueryToDisplayProductDelivery();
            orderBottomNavigationView.setOnNavigationItemSelectedListener(this);
            orderBottomNavigationView.setSelectedItemId(R.id.accept);
            backSoloBottomNavigationView.setVisibility(View.GONE);
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


    String getQueryToDisplayHistoryOrders() {
        return "SELECT * FROM [remakePyszne].[dbo].[Orders] WHERE userid=" + users.getId() + " ORDER BY [orderDate]DESC, [orderTime]DESC;";
    }

    String getQueryToDisplayOrder() {
        return "SELECT * FROM [remakePyszne].[dbo].[Orders] WHERE restaurantid=" + restaurants.getRestaurantID()  + " ORDER BY [orderDate]DESC, [orderTime]DESC;";
    }

    String getQueryToDisplayProductDelivery() {
        return "SELECT * FROM [remakePyszne].[dbo].[Orders] WHERE providerid='null' ORDER BY [orderDate]DESC, [orderTime]DESC;";
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (users.getRole().equals("restaurant manager") || users.getRole().equals("provider")) {
            updateStateOrder(ordersArrayList.get(i));
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accept:
                orderBottomNavigationView.getMenu().findItem(R.id.accept).setChecked(true);
                if (users.getRole().equals("restaurant manager")) {
                    nextState = "gotowe do dostarczenia";
                } else if (users.getRole().equals("provider")) {
                    nextState = "dostawca jest w twojej okolicy";
                }
                break;
            case R.id.forward:
                orderBottomNavigationView.getMenu().findItem(R.id.forward).setChecked(true);
                if (users.getRole().equals("restaurant manager")) {
                    nextState = "odebrano przez dostawcę";
                } else if (users.getRole().equals("provider")) {
                    nextState = "dostarczono";
                }
                break;
            case R.id.back:
                openActivity(RestaurantActivity.class);
        }
        return false;
    }

    String updateStateOrder(Orders orders){
        if(users.getRole().equals("provider") && nextState.equals("dostawca jest w twojej okolic"))
            return "UPDATE [remakePyszne].[dbo].[Orders] SET state='" + nextState + "' providerid='"+users.getId()+"' WHERE orderid="+orders.getOrderID();
        else
            return "UPDATE [remakePyszne].[dbo].[Orders] SET state='" + nextState + "' WHERE orderid="+orders.getOrderID();
    }

    public void openActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        intent.putExtra("currentUser", users);

        if (users.getRole().equals("user")) {
            intent.putExtra("currentAddress", addresses);
        }
        intent.putExtra("currentRestaurant", restaurants);
        startActivity(intent);
    }
}