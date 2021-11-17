package com.example.remakepyszne.sql;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.remakepyszne.util.Users;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

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
                        .email(resultSet.getString(4))
                        .phoneNumber(resultSet.getInt(5))
                        .build();
            }
        } else {
            Log.d("Error database", "Error query");
        }
        if(users.getPassword() == null)
            users = null;
        return users ;
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
