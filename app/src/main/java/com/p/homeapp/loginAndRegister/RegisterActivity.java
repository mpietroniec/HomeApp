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
import com.p.homeapp.helpers.AccountDataValidator;
import com.p.homeapp.helpers.BCryptHelper;

import java.time.LocalDateTime;

public class RegisterActivity extends AppCompatActivity {

    EditText eTxtLogin, eTxtEmail, eTxtPassword, eTxtConfirmPassword;
    Button btnRegister;
    TextView txtLogin;

    DBHelper dbHelper;
    AccountDataValidator accountDataValidator = new AccountDataValidator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        eTxtLogin = findViewById(R.id.etxt_login);
        eTxtEmail = findViewById(R.id.etxt_email);
        eTxtPassword = findViewById(R.id.etxt_password);
        eTxtConfirmPassword = findViewById(R.id.etxt_confirmPassword);

        btnRegister = findViewById(R.id.btn_register);
        txtLogin = findViewById(R.id.txt_signIn);

        dbHelper = new DBHelper(RegisterActivity.this);

        btnRegister.setOnClickListener((v)->{

            String login = eTxtLogin.getText().toString();
            String email = eTxtEmail.getText().toString();
            String password = eTxtPassword.getText().toString();
            String confirmPassword = eTxtConfirmPassword.getText().toString();

            User user = new User();
            user.setLogin(login);
            user.setEmail(email);
            user.setCreateDate(LocalDateTime.now());
            user.setRole("ROLE_USER");
            BCryptHelper.hashPassword(user, password);

            if(accountDataValidator.validateRegisterData(getApplicationContext(), password, confirmPassword, user, dbHelper)){
                dbHelper.addOne(user);

                dbHelper.close();

                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                Toast.makeText(RegisterActivity.this, R.string.register_successful, Toast.LENGTH_SHORT).show();
            }
        });

        txtLogin.setOnClickListener((v)-> {
            Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(loginIntent);
        });
    }
}