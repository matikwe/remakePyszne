package com.example.remakepyszne;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionHelper {
    Connection con;
    String uname, pass, ip, port, database;

    @SuppressLint("NewApi")
    public Connection connectionClass(){
        ip = "192.168.0.175";
        database = "remakePyszne";
        uname = "sa";
        pass = "123";
        port = "1433";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Connection connection = null;
        String connectionURL = null;

        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connectionURL= "jdbc:jtds:sqlserver://"+ ip + ":"+ port+";"+ "databasename="+ database+";user="+uname+";password="+pass+";";
            connection = DriverManager.getConnection(connectionURL);

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }
        return connection;
    }
}
