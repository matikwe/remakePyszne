package com.example.remakepyszne.activities;

import android.content.Intent;
import android.os.Bundle;
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
    String emptyEditText = "";
    private static final String incorrectLoginOrPassword = "Dane logowania nie są poprawne";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextLogin = (EditText) findViewById(R.id.editTextLogin);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
    }

    public void tryLoginOnClick(View v) throws SQLException {
        if (editTextLogin.getText().toString().isEmpty()) {
            emptyEditText += "Wpisz login";
        }
        if (editTextPassword.getText().toString().isEmpty()) {
            emptyEditText += " Wpisz hasło";
        }

        if (emptyEditText.equals("")) {
            String query = "SELECT * FROM [remakePyszne].[dbo].[users] WHERE login='" + editTextLogin.getText() + "';";
            List<Users> usersList = new QueryHelper(query).tryLoginToDataBase();

            if (!(usersList.isEmpty())) {
                if (String.valueOf(usersList.get(0).getPassword()).equals(editTextPassword.getText().toString())) {
                    openActivityHome();
                } else {
                    Toast.makeText(getApplicationContext(), incorrectLoginOrPassword, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), incorrectLoginOrPassword, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), emptyEditText, Toast.LENGTH_LONG).show();
        }
        emptyEditText = "";
    }

    public void openActivityRegister(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void openActivityHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}