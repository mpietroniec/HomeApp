package com.p.homeapp.loginAndRegister;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.p.homeapp.DB.DBHelper;
import com.p.homeapp.R;
import com.p.homeapp.entities.User;

import java.time.LocalDateTime;

public class RegisterActivity extends AppCompatActivity {

    EditText eTxtLogin, eTxtEmail, eTxtPassword;
    Button btnRegister;
    TextView txtLogin;
    DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        eTxtLogin = findViewById(R.id.etxt_login);
        eTxtEmail = findViewById(R.id.etxt_email);
        eTxtPassword = findViewById(R.id.etxt_password);
        btnRegister = findViewById(R.id.btn_register);
        txtLogin = findViewById(R.id.txt_signIn);

        dbHelper = new DBHelper(RegisterActivity.this);

        btnRegister.setOnClickListener((v)->{
            User user = new User();
            user.setLogin(eTxtLogin.getText().toString());
            user.setEmail(eTxtEmail.getText().toString());
            user.setPassword(eTxtPassword.getText().toString());
            user.setCreateDate(LocalDateTime.now());
            user.setRole("ROLE_USER");

            boolean success = dbHelper.addOne(user);
            Toast.makeText(RegisterActivity.this, "Success: " + success, Toast.LENGTH_SHORT).show();

            eTxtLogin.setText("");
            eTxtEmail.setText("");
            eTxtPassword.setText("");

            dbHelper.close();
        });

        txtLogin.setOnClickListener((v)-> {
            Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(loginIntent);
        });
    }
}