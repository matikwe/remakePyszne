package com.example.remakepyszne.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.mlkit.common.sdkinternal.SharedPrefManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class OrderProductActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, BottomNavigationView.OnNavigationItemSelectedListener {
    private Users users;
    private Addresses addresses;
    private Restaurants restaurants;
    protected ArrayList<Orders> ordersArrayList;
    BottomNavigationView orderBottomNavigationView, backSoloBottomNavigationView, backAndLogoutBottomNavigationView;
    ListView listViewOrders;
    TextView title;
    String query, nextState, currentState;
    List<String> states;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_product);
        loadTypeState();
        loadNav();
        initiation();

    }

    private void loadNav() {
        orderBottomNavigationView = (BottomNavigationView) findViewById(R.id.orderBottomNavigationView);
        orderBottomNavigationView.setSelectedItemId(R.id.accept);
    }

    private void initiation() {
        Intent intent = getIntent();
        users = intent.getParcelableExtra("currentUser");
        listViewOrders = (ListView) findViewById(R.id.listViewOrderProduct);
        title = (TextView) findViewById(R.id.titleOrder);
        backSoloBottomNavigationView = (BottomNavigationView) findViewById(R.id.backSoloBottomNavigationView);
        backAndLogoutBottomNavigationView = (BottomNavigationView) findViewById(R.id.backAndLogoutBottomNavigationView);

        if (users.getRole().equals("user")) {
            title.setText("Historia zamówień");
            addresses = intent.getParcelableExtra("currentAddress");
            query = getQueryToDisplayHistoryOrders();
            backSoloBottomNavigationView.setOnNavigationItemSelectedListener(this);
            orderBottomNavigationView.setVisibility(View.GONE);
            backAndLogoutBottomNavigationView.setVisibility(View.GONE);
        } else if (users.getRole().equals("restaurant manager")) {
            title.setText("Zamówione produkty");
            restaurants = intent.getParcelableExtra("currentRestaurant");
            if (currentState == null) {
                currentState = states.get(0);
                nextState = states.get(1);
            }
            query = getQueryToDisplayOrder();
            orderBottomNavigationView.setOnNavigationItemSelectedListener(this);
            backSoloBottomNavigationView.setVisibility(View.GONE);
            backAndLogoutBottomNavigationView.setVisibility(View.GONE);
        } else if (users.getRole().equals("provider")) {
            title.setText("Aktualne dostawy");
            if (currentState == null) {
                currentState = states.get(2);
                nextState = states.get(3);
            }
            query = getQueryToDisplayProductDelivery();
            backAndLogoutBottomNavigationView.setOnNavigationItemSelectedListener(this);
            backSoloBottomNavigationView.setVisibility(View.GONE);
            orderBottomNavigationView.setVisibility(View.GONE);
        }

        try {
            ordersArrayList = new QueryHelper(query).tryLoginToDataBaseForOrders();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        loadArrayAdapter();
    }

    private void loadArrayAdapter() {
        OrdersAdapter ordersAdapter = new OrdersAdapter(this, ordersArrayList, nextState);
        listViewOrders.setAdapter(ordersAdapter);
        listViewOrders.setOnItemClickListener(this);
    }

    String getQueryToDisplayHistoryOrders() {
        return "SELECT * FROM [remakePyszne].[dbo].[Orders] WHERE userid=" + users.getId() + " ORDER BY [orderDate]DESC, [orderTime]DESC;";
    }

    String getQueryToDisplayOrder() {
        return "SELECT * FROM [remakePyszne].[dbo].[Orders] WHERE restaurantid=" + restaurants.getRestaurantID() + " AND state LIKE '%" + currentState + "%' ORDER BY [orderDate]DESC, [orderTime]DESC;";
    }

    String getQueryToDisplayProductDelivery() {
        return "SELECT * FROM [remakePyszne].[dbo].[Orders] WHERE state LIKE '%" + currentState + "%' ORDER BY [orderDate]DESC, [orderTime]DESC;";
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (users.getRole().equals("restaurant manager") || users.getRole().equals("provider")) {
            new QueryHelper(updateStateOrder(ordersArrayList.get(i))).tryConnectToDatabase();
            openActivity(OrderProductActivity.class);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accept:
                orderBottomNavigationView.getMenu().findItem(R.id.accept).setChecked(true);
                if (users.getRole().equals("restaurant manager")) {
                    currentState = states.get(0);
                    nextState = states.get(1);
                } else if (users.getRole().equals("provider")) {
                    currentState = states.get(2);
                    nextState = states.get(3);
                }
                initiation();
                break;
            case R.id.forward:
                orderBottomNavigationView.getMenu().findItem(R.id.forward).setChecked(true);
                if (users.getRole().equals("restaurant manager")) {
                    currentState = states.get(1);
                    nextState = states.get(2);
                } else if (users.getRole().equals("provider")) {
                    currentState = states.get(3);
                    nextState = states.get(4);
                }
                initiation();
                break;
            case R.id.back:
                if (users.getRole().equals("provider")) {
                    Toast.makeText(getApplicationContext(), "Nie masz uprawnień do powrotu...", Toast.LENGTH_LONG).show();
                } else {
                    orderBottomNavigationView.getMenu().findItem(R.id.back).setChecked(true);
                    openActivity(RestaurantActivity.class);
                }
                break;
            case R.id.logout:
                logout();
                break;

        }
        return false;
    }

    private void logout() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    String updateStateOrder(Orders orders) {
        if (users.getRole().equals("provider") && currentState.equals(states.get(2))) {
            return "UPDATE [remakePyszne].[dbo].[Orders] SET state='" + nextState + "', providerid=" + users.getId() + " WHERE orderid=" + orders.getOrderID();
        } else
            return "UPDATE [remakePyszne].[dbo].[Orders] SET state='" + nextState + "' WHERE orderid=" + orders.getOrderID();
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

    private void loadTypeState() {
        states = new LinkedList<>();
        states.add("w realizacji");
        states.add("gotowe do dostarczenia");
        states.add("odebrano przez dostawcę");
        states.add("dostawa jest w twojej okolicy");
        states.add("dostarczono");
    }
}