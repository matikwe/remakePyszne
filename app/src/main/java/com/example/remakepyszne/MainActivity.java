package com.example.remakepyszne;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    EditText editTextLogin;
    EditText editTextPassword;
    TextView textViewErrorLogin;
    String records = "",error="";
    Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextLogin = (EditText) findViewById(R.id.editTextLogin);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textViewErrorLogin = (TextView) findViewById(R.id.textViewErrorLogin);

        }
        public void tryLoginOnClick(View v){
            //if(editTextLogin.toString().isEmpty() || editTextPassword.toString().isEmpty()){
               //textViewErrorLogin.setText(editTextLogin.getText().toString());

            //}else{
                //textViewErrorLogin.setText("coś nie sprawdza");
            //}



            try {
                ConnectionHelper connectionHelper = new ConnectionHelper();
                connection = connectionHelper.connectionClass();
                if(connection != null){
                    String query = "select * from [remakePyszne].[dbo].[user];";
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);

                    while (resultSet.next()){
                        textViewErrorLogin.setText(resultSet.getString(1));
                    }
                }else {
                    textViewErrorLogin.setText("nie laczy z baza");
                }

            }
            catch(Exception e)

            {

                error = e.toString();
                textViewErrorLogin.setText(e.toString());

            }


        }
}