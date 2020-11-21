package com.p.homeapp.loginAndRegister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.p.homeapp.R;

public class RegisterActivity extends AppCompatActivity {

    EditText eTxtLogin, eTxtEmail, eTxtPassword;
    Button btnRegister;
    TextView txtLogin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        eTxtLogin = findViewById(R.id.etxt_login);
        eTxtEmail = findViewById(R.id.etxt_email);
        eTxtPassword = findViewById(R.id.etxt_password);
        btnRegister = findViewById(R.id.btn_register);
        txtLogin = findViewById(R.id.txt_signIn);

        btnRegister.setOnClickListener((v)->{

        });

        txtLogin.setOnClickListener((v)-> {
            Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(loginIntent);
        });
    }
}