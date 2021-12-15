package com.example.remakepyszne.sql;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.remakepyszne.util.Addresses;
import com.example.remakepyszne.util.Orders;
import com.example.remakepyszne.util.Products;
import com.example.remakepyszne.util.Restaurants;
import com.example.remakepyszne.util.ShopCart;
import com.example.remakepyszne.util.Users;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class QueryHelper {
    private String query;
    private Connection connection;

    public QueryHelper(String query) {
        this.query = query;
    }

    @SuppressLint("LongLogTag")
    public Users tryLoginToDataBaseForUsers() throws SQLException {
        Users users = new Users();
        ResultSet resultSet = tryConnectToDatabase();
        if (resultSet != null) {
            while (resultSet.next()) {
                users = new Users.Builder()
                        .id(resultSet.getInt(1))
                        .login(resultSet.getString(2))
                        .password(resultSet.getString(3))
                        .role(resultSet.getString(4))
                        .email(resultSet.getString(5))
                        .phoneNumber(resultSet.getInt(6))
                        .build();
            }
        } else {
            Log.d("Error database", "Error query");
        }
        if (users.getPassword() == null)
            users = null;
        return users;
    }

    public Addresses tryLoginToDataBaseForAddresses() throws SQLException {
        Addresses addresses = new Addresses();
        ResultSet resultSet = tryConnectToDatabase();
        if (resultSet != null) {
            while (resultSet.next()) {
                addresses = new Addresses.Builder()
                        .addressID(resultSet.getInt(1))
                        .userID(resultSet.getInt(2))
                        .street(resultSet.getString(3))
                        .number(resultSet.getString(4))
                        .city(resultSet.getString(5))
                        .zip(resultSet.getString(6))
                        .build();
            }
        } else {
            Log.d("Error database", "Error query");
        }
        if (addresses.getStreet() == null)
            addresses = null;
        return addresses;
    }

    public ArrayList<Restaurants> tryLoginToDataBaseForRestaurants() throws SQLException {
        Restaurants restaurants = new Restaurants();
        ArrayList<Restaurants> restaurantsList = new ArrayList<>();
        ResultSet resultSet = tryConnectToDatabase();
        if (resultSet != null) {
            while (resultSet.next()) {
                restaurants = new Restaurants.Builder()
                        .restaurantID(resultSet.getInt(1))
                        .nameRestaurant(resultSet.getString(2))
                        .type(resultSet.getString(3))
                        .street(resultSet.getString(4))
                        .number(resultSet.getString(5))
                        .city(resultSet.getString(6))
                        .zip(resultSet.getString(7))
                        .openingHour(resultSet.getTime(8))
                        .closingHour(resultSet.getTime(9))
                        .image(resultSet.getString(10))
                        .restaurantManagerid(resultSet.getInt(11))
                        .build();
                restaurantsList.add(restaurants);
            }
        } else {
            Log.d("Error database", "Error query");
        }
        return restaurantsList;
    }

    public ArrayList<Products> tryLoginToDataBaseForProducts() throws SQLException {
        Products products = new Products();
        ArrayList<Products> productsArrayList = new ArrayList<>();
        ResultSet resultSet = tryConnectToDatabase();
        if (resultSet != null) {
            while (resultSet.next()) {
                products = new Products.Builder()
                        .productID(resultSet.getInt("productid"))
                        .restaurantID(resultSet.getInt("restaurantid"))
                        .nameProduct(resultSet.getString("nameProduct"))
                        .category(resultSet.getString("category"))
                        .price(resultSet.getFloat("price"))
                        .image(resultSet.getString("imageProduct"))
                        .build();
                productsArrayList.add(products);
            }
        } else {
            Log.d("Error database", "Error query");
        }
        return productsArrayList;
    }

    public ArrayList<ShopCart> tryLoginToDataBaseForShopCart() throws SQLException {
        ShopCart shopCart = new ShopCart();
        ArrayList<ShopCart> shopCartArrayList = new ArrayList<>();
        ResultSet resultSet = tryConnectToDatabase();
        if (resultSet != null) {
            while (resultSet.next()) {
                shopCart = new ShopCart.Builder()
                        .shopCartid(resultSet.getInt("shopCartid"))
                        .userid(resultSet.getInt("userid"))
                        .productID(resultSet.getInt("productid"))
                        .quantity(resultSet.getInt("quantity"))
                        .restaurantID(resultSet.getInt("restaurantid"))
                        .price(resultSet.getFloat("price"))
                        .nameProduct(resultSet.getString("nameProduct"))
                        .imageProduct(resultSet.getString("imageProduct"))
                        .build();
                shopCartArrayList.add(shopCart);
            }
        } else {
            Log.d("Error database", "Error query");
        }
        return shopCartArrayList;
    }

    public int getQuantity() throws SQLException {
        int quantity = 0;
        ResultSet resultSet = tryConnectToDatabase();
        while (resultSet.next()) {
            quantity = resultSet.getInt("quantity");
        }
        return quantity;
    }

    public ArrayList<Orders> tryLoginToDataBaseForOrders() throws SQLException {
        Orders orders = new Orders();
        ArrayList<Orders> ordersArrayList = new ArrayList<>();
        ResultSet resultSet = tryConnectToDatabase();
        if (resultSet != null) {
            while (resultSet.next()) {
                orders = new Orders.Builder()
                        .orderID(resultSet.getInt("orderid"))
                        .userID(resultSet.getInt("userid"))
                        .restaurantID(resultSet.getInt("restaurantid"))
                        .addressID(resultSet.getInt("addressid"))
                        .description(resultSet.getString("description"))
                        .orderTime(resultSet.getTime("orderTime"))
                        .orderDate(resultSet.getDate("orderDate"))
                        .state(resultSet.getString("state"))
                        .totalPrice(resultSet.getFloat("totalPrice"))
                        .providerID(resultSet.getInt("providerid"))
                        .expectedDeliveryTime(resultSet.getTime("expectedDeliveryTime"))
                        .build();
                ordersArrayList.add(orders);
            }
        } else {
            Log.d("Error database", "Error query");
        }
        return ordersArrayList;
    }

    @SuppressLint("LongLogTag")
    public ResultSet tryConnectToDatabase() {
        ResultSet resultSet = null;
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connection = connectionHelper.connectionClass();
            if (connection != null) {
                Statement statement = connection.createStatement();
                resultSet = statement.executeQuery(query);
            } else {
                Log.d("Error database", "Error connect with database");
            }
        } catch (Exception e) {
            Log.d("Exception with connect to database: ", e.toString());
        }
        return resultSet;
    }
}
