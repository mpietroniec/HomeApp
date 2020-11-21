package com.p.homeapp.loginAndRegister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.p.homeapp.R;

public class LoginActivity extends AppCompatActivity {

    TextView txtRegister;
    EditText eTxtLogin, eTxtPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtRegister = findViewById(R.id.txt_register);
        txtRegister.setOnClickListener((v) -> {
            Intent RegisterIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(RegisterIntent);
        });
    }
}
