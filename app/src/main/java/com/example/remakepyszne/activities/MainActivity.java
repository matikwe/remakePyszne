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

public class MainActivity extends AppCompatActivity {

    EditText editTextLogin, editTextPassword;
    private boolean notEmptyData = true;
    private static final String incorrectLoginOrPassword = "Dane logowania nie są poprawne";
    private static final String emptyLogin = "Uzupełnij login";
    private static final String emptyPassword = "Uzupełnij hasło";
    private Users users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextLogin = (EditText) findViewById(R.id.editTextLogin);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

    }

    public void tryLoginOnClick(View v) throws SQLException {
        if (editTextLogin.getText().toString().isEmpty()) {
            notEmptyData = false;
            Toast.makeText(getApplicationContext(), emptyLogin, Toast.LENGTH_LONG).show();
        }
        if (editTextPassword.getText().toString().isEmpty()) {
            if (notEmptyData) {
                Toast.makeText(getApplicationContext(), emptyPassword, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), emptyLogin + ". " + emptyPassword, Toast.LENGTH_LONG).show();
            }
            notEmptyData = false;
        }
        sendRequestToDatabaseAndEquals(notEmptyData);
        notEmptyData = true;
    }

    private void sendRequestToDatabaseAndEquals(boolean notEmptyData) throws SQLException {
        if (notEmptyData) {
            String query = "SELECT * FROM [remakePyszne].[dbo].[users] WHERE login='" + editTextLogin.getText() + "';";
            users = new QueryHelper(query).tryLoginToDataBaseForUsers();

            if (!(users == null)) {
                if (String.valueOf(users.getPassword()).equals(editTextPassword.getText().toString())) {
                    if(users.getRole().equals("user"))
                        openActivity(AddressActivity.class);
                    else if(users.getRole().equals("provider"))
                        openActivity(OrderProductActivity.class);
                    else if (users.getRole().equals("restaurant manager")){
                        openActivity(RestaurantActivity.class);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), incorrectLoginOrPassword, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), incorrectLoginOrPassword, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void openActivityRegister(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void openActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        intent.putExtra("currentUser", users);
        startActivity(intent);
    }
}