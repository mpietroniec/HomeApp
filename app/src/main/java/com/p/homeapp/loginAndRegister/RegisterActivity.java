package com.p.homeapp.loginAndRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.p.homeapp.MainActivity;
import com.p.homeapp.R;
import com.p.homeapp.entities.User;
import com.p.homeapp.helpers.AccountDataValidator;

import java.time.LocalDateTime;

public class RegisterActivity extends AppCompatActivity {

    EditText eTxtLogin, eTxtEmail, eTxtPassword, eTxtConfirmPassword;
    Button btnRegister;
    TextView txtLogin;

    AccountDataValidator accountDataValidator;

    private DatabaseReference mRootRef;
    private FirebaseAuth mAuth;

    ProgressDialog progressDialog;

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

        accountDataValidator = new AccountDataValidator();

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);


        txtLogin.setOnClickListener((v) -> {
            Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        });


        btnRegister.setOnClickListener((v) -> {

            String login = eTxtLogin.getText().toString();
            String email = eTxtEmail.getText().toString();
            String password = eTxtPassword.getText().toString();
            String confirmPassword = eTxtConfirmPassword.getText().toString();

            User user = new User();
            user.setLogin(login);
            user.setEmail(email);
            user.setCreateDate(LocalDateTime.now());

            if (accountDataValidator.validateRegisterData(getApplicationContext(), password, confirmPassword, user)) {
                registerUser(user, password);
            }
        });
    }

    private void registerUser(User user, String password) {
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(user.getEmail(), password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                user.setId(mAuth.getCurrentUser().getUid());
                mRootRef.child("users").child(mAuth.getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Register successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
