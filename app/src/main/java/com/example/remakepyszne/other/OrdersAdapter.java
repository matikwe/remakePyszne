package com.example.remakepyszne.other;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.remakepyszne.R;
import com.example.remakepyszne.sql.QueryHelper;
import com.example.remakepyszne.util.Addresses;
import com.example.remakepyszne.util.Orders;
import com.example.remakepyszne.util.Restaurants;
import com.example.remakepyszne.util.Users;

import java.sql.SQLException;
import java.util.ArrayList;

public class OrdersAdapter extends ArrayAdapter<Orders> {
    private Restaurants restaurants;
    private Users users;
    private Addresses addresses;
    private Orders orders;
    private String nextState;

    public OrdersAdapter(Context context, ArrayList<Orders> ordersArrayList, String nextState) {
        super(context, 0, ordersArrayList);
        this.nextState = nextState;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        orders = getItem(position);

        try {
            restaurants = getRestaurants();
            users = getUsers();
            addresses = getAddressesForUser();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_order, parent, false);
        }

        LinearLayout linearLayoutStateButton = (LinearLayout) convertView.findViewById(R.id.linearLayoutStateButton);

        TextView nameRestaurant = (TextView) convertView.findViewById(R.id.nameRestaurant);
        TextView addressRest = convertView.findViewById(R.id.addressRest);

        TextView nameUser = (TextView) convertView.findViewById(R.id.nameUser);
        TextView addressUser = convertView.findViewById(R.id.addressUser);

        TextView description = (TextView) convertView.findViewById(R.id.description);
        TextView state = (TextView) convertView.findViewById(R.id.state);
        TextView stateButton = (TextView) convertView.findViewById(R.id.stateButton);


        nameRestaurant.setText(restaurants.getNameRestaurant());
        String addressString = "ul. " + restaurants.getStreet() + " " + restaurants.getNumber() + "\n"
                + restaurants.getZip() + " " + restaurants.getCity();
        addressRest.setText(addressString);

        nameUser.setText(users.getLogin());

        String addressStringUser = "ul. " + addresses.getStreet() + " " + addresses.getNumber() + "\n"
                + addresses.getZip() + " " + addresses.getCity();
        addressUser.setText(addressStringUser);

        //telefon mail

        description.setText(orders.getDescription());
        state.setText("Status zamówienia: " + orders.getState());

        if (users.getRole().equals("user"))
            linearLayoutStateButton.setVisibility(View.GONE);
        else
            linearLayoutStateButton.setVisibility(View.VISIBLE);
        stateButton.setText("eeeeeeeeeeeeeeeeeeeeeeeee");

        return convertView;
    }

    private Restaurants getRestaurants() throws SQLException {
        ArrayList<Restaurants> restaurantsArrayList = new QueryHelper(queryToGetRestaurant()).tryLoginToDataBaseForRestaurants();
        return restaurantsArrayList.get(0);
    }

    private Users getUsers() throws SQLException {
        return new QueryHelper(queryToGetUser()).tryLoginToDataBaseForUsers();
    }

    private Addresses getAddressesForUser() throws SQLException {
        return new QueryHelper(queryToGetAddressForUser()).tryLoginToDataBaseForAddresses();
    }

    private String queryToGetRestaurant() {
        return "SELECT * FROM [remakePyszne].[dbo].[Restaurants] INNER JOIN [remakePyszne].[dbo].[Orders] " +
                "ON [remakePyszne].[dbo].[Restaurants].restaurantid = [remakePyszne].[dbo].[Orders].restaurantid " +
                "WHERE [remakePyszne].[dbo].[Restaurants].restaurantid=" + orders.getRestaurantID();
    }

    private String queryToGetUser() {
        return "SELECT * FROM [remakePyszne].[dbo].[Users] INNER JOIN [remakePyszne].[dbo].[Orders] " +
                "ON [remakePyszne].[dbo].[Users].userid = [remakePyszne].[dbo].[Orders].userid " +
                "WHERE [remakePyszne].[dbo].[Users].userid=" + orders.getUserID();
    }

    private String queryToGetAddressForUser() {
        return "SELECT * FROM [remakePyszne].[dbo].[Addresses] INNER JOIN [remakePyszne].[dbo].[Orders] " +
                "ON [remakePyszne].[dbo].[Addresses].addressid = [remakePyszne].[dbo].[Orders].addressid " +
                "WHERE [remakePyszne].[dbo].[Addresses].addressid=" + orders.getAddressID();
    }
}
