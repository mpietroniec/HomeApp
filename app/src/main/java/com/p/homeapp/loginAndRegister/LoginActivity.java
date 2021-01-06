package com.p.homeapp.loginAndRegister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.p.homeapp.DB.DBHelper;
import com.p.homeapp.R;
import com.p.homeapp.entities.User;
import com.p.homeapp.helpers.BCryptHelper;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class LoginActivity extends AppCompatActivity {

    TextView txtRegister;
    EditText eTxtLogin, eTxtPassword;
    Button btnLogin;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtRegister = findViewById(R.id.txt_register);
        eTxtLogin = findViewById(R.id.etxt_loign);
        eTxtPassword = findViewById(R.id.etxt_password);
        btnLogin = findViewById(R.id.btn_login);

        dbHelper = new DBHelper(LoginActivity.this);

        btnLogin.setOnClickListener(v -> {
            String username = eTxtLogin.getText().toString();
            String password = eTxtPassword.getText().toString();
            User user = dbHelper.getUser(username);
            if(BCryptHelper.checkPassword(password, user.getPassword())){
                Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_LONG).show();
            }
        });

        txtRegister.setOnClickListener((v) -> {
            Intent RegisterIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(RegisterIntent);
        });
    }

}
