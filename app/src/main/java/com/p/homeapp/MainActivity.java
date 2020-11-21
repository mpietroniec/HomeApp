package com.p.homeapp;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.p.homeapp.entities.User;
import com.p.homeapp.loginAndRegister.LoginActivity;


public class MainActivity extends AppCompatActivity {

    TextView txtHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtHome = findViewById(R.id.txtHome);
        txtHome.setOnClickListener((v) ->{
            Intent LoginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(LoginIntent);
        });
    }
}
