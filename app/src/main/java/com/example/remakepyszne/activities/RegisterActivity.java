package com.example.remakepyszne.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.remakepyszne.R;
import com.example.remakepyszne.sql.QueryHelper;
import com.example.remakepyszne.util.Users;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText editTextLoginRegisterPage, editTextPasswordRegisterPage1,
            editTextPasswordRegisterPage2, editTextEmailAddressRegisterPage, editTextPhoneRegisterPage;
    Spinner spinner;
    private static final String emptyEditText = "Uzupełnij dane do rejestracji";
    private static final String notSimilarPassword = "Hasła się różnią";
    private static final String passwordTooShort = "Hasło musi zawierać 8 znaków";
    private static final String phoneNumberTooShort = "Telefon musi zawierać 9 cyfr";
    private static final String badFormatEmail = "Email być w formacie domena@yxz.pl";
    private boolean nextActivity = true;
    private boolean isNotDuplicated = true;
    private Users users;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextLoginRegisterPage = (EditText) findViewById(R.id.editTextLoginRegisterPage);
        editTextPasswordRegisterPage1 = (EditText) findViewById(R.id.editTextPasswordRegisterPage1);
        editTextPasswordRegisterPage2 = (EditText) findViewById(R.id.editTextPasswordRegisterPage2);
        editTextEmailAddressRegisterPage = (EditText) findViewById(R.id.editTextEmailAddressRegisterPage);
        editTextPhoneRegisterPage = (EditText) findViewById(R.id.editTextPhoneRegisterPage);
        spinner = (Spinner) findViewById(R.id.userRoleSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        users = new Users();
    }

    public void tryRegisterOnClick(View v) throws SQLException {
        if (editTextLoginRegisterPage.getText().toString().isEmpty() || editTextPasswordRegisterPage1.getText().toString().isEmpty() ||
                editTextPasswordRegisterPage2.getText().toString().isEmpty() || editTextEmailAddressRegisterPage.getText().toString().isEmpty() ||
                editTextPhoneRegisterPage.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), emptyEditText, Toast.LENGTH_LONG).show();
            nextActivity = false;
        } else {
            if (editTextPasswordRegisterPage1.getText().toString().equals(editTextPasswordRegisterPage2.getText().toString())) {
                if (editTextPasswordRegisterPage1.getText().toString().length() < 7) {
                    Toast.makeText(getApplicationContext(), passwordTooShort, Toast.LENGTH_LONG).show();
                    nextActivity = false;
                }
            } else {
                Toast.makeText(getApplicationContext(), notSimilarPassword, Toast.LENGTH_LONG).show();
                nextActivity = false;
            }
            if (!emailValidation(editTextEmailAddressRegisterPage.getText().toString())) {
                Toast.makeText(getApplicationContext(), badFormatEmail, Toast.LENGTH_LONG).show();
                nextActivity = false;
            }
            if (editTextPhoneRegisterPage.getText().toString().length() < 8) {
                Toast.makeText(getApplicationContext(), phoneNumberTooShort, Toast.LENGTH_LONG).show();
                nextActivity = false;
            }

            validationDuplicatedData(nextActivity);
            nextActivity = true;
        }
    }

    void validationDuplicatedData(boolean nextActivity) throws SQLException {
        if (nextActivity) {
            if (dataIsDuplicated("login", editTextLoginRegisterPage.getText().toString()))
                isNotDuplicated = false;

            if (dataIsDuplicated("email", editTextEmailAddressRegisterPage.getText().toString()))
                isNotDuplicated = false;

            if (dataIsDuplicated("phone_number", editTextPhoneRegisterPage.getText().toString()))
                isNotDuplicated = false;

            if (isNotDuplicated) {
                String query = "INSERT INTO Users (login, password, email, phone_number) values ('" + editTextLoginRegisterPage.getText().toString() +
                        "','" + editTextPasswordRegisterPage1.getText().toString() + "','" + editTextEmailAddressRegisterPage.getText().toString() +
                        "','" + editTextPhoneRegisterPage.getText().toString() + "');";
                insertIntoDatabase(query);
                openMainActivityMethod();
            } else {
                isNotDuplicated = true;
            }
        }
    }

    public boolean dataIsDuplicated(String col, String value) throws SQLException {
        String query = "SELECT * FROM [remakePyszne].[dbo].[users] WHERE " + col + "='" + value + "';";
        users = new QueryHelper(query).tryLoginToDataBaseForUsers();

        if (users == null) {
            Log.d("State list: " + col, " empty");
            return false;
        } else {
            Log.d("State list: " + col, " notEmpty");
            Toast.makeText(getApplicationContext(), "Podany " + col + " jest już zajęty !!!", Toast.LENGTH_LONG).show();
            return true;
        }
    }

    public boolean emailValidation(String email) {
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void insertIntoDatabase(String query) throws SQLException {
        new QueryHelper(query).tryLoginToDataBaseForUsers();
    }

    public void openMainActivity(View v) {
        openMainActivityMethod();
    }

    void openMainActivityMethod() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        role = adapterView.getItemAtPosition(i).toString();
        Log.d("Role", role);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}