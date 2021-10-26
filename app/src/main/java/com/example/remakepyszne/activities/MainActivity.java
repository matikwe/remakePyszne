package com.example.remakepyszne.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.remakepyszne.R;
import com.example.remakepyszne.sql.QueryHelper;
import com.example.remakepyszne.util.Users;

import java.sql.SQLException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText editTextLogin;
    EditText editTextPassword;
    String error = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextLogin = (EditText) findViewById(R.id.editTextLogin);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
    }

    public void tryLoginOnClick(View v) throws SQLException {
        if (editTextLogin.getText().toString().isEmpty()) {
            error += "Wpisz login";
        }
        if (editTextPassword.getText().toString().isEmpty()) {
            error += " Wpisz hasło";
        }
        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();

        if (error.equals("")) {
            String query = "select * from [remakePyszne].[dbo].[users];";
            List<Users> usersList = new QueryHelper(query).tryLoginToDataBase();

            //wywolanie
            Log.d("Msg:", String.valueOf(usersList.get(0).getId()));
            openActivityHome();
        }
        error = "";
    }

    public void openActivityRegister(View v){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }

    public void openActivityHome() {
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
    }
}