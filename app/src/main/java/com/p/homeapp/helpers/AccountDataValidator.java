package com.p.homeapp.helpers;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.p.homeapp.R;
import com.p.homeapp.entities.User;

import java.util.regex.Pattern;

public class AccountDataValidator extends AppCompatActivity {

        public boolean validateRegisterData(Context context, String password, String confirmPassword, User user){
        if(isPasswordConfirmed(context, password, confirmPassword) &&
                isRegisterFieldsEmpty(context, user.getLogin(), user.getEmail(), password) &&
                isLoginMatches(context, user.getLogin()) &&
                isEmailMatches(context, user.getEmail()) &&
                isPasswordMatches(context, password)){
            return true;
        }
        return false;
    }

    private boolean isPasswordConfirmed(Context context, String password, String confirmPassword){
        if(password.equals(confirmPassword)){
            return true;
        }
        Toast.makeText(context, R.string.confirmed_password_incorrect, Toast.LENGTH_LONG).show();
        return false;
    }

    private boolean isRegisterFieldsEmpty(Context context, String login, String email, String password){
        if(TextUtils.isEmpty(login)){
            Toast.makeText(context, R.string.login_field_empty, Toast.LENGTH_LONG).show();
            return false;
        }
        if(email == null || email.isEmpty() || email.equals("")){
            Toast.makeText(context, R.string.email_field_empty, Toast.LENGTH_LONG).show();
            return false;
        }
        if(password == null || password.isEmpty() || password.equals("")){
            Toast.makeText(context, R.string.password_field_empty, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean isLoginMatches(Context context, String login){
        Pattern pattern = Pattern.compile("^(?=.*[A-Za-z0-9]$)[A-Za-z][A-Za-z\\d.-]{0,19}$");
        if (pattern.matcher(login).matches()){
            return true;
        }
        Toast.makeText(context, R.string.login_not_match, Toast.LENGTH_LONG).show();
        return false;
    }

    private boolean isEmailMatches(Context context, String email){
        Pattern pattern = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?");
        if(pattern.matcher(email).matches()){
            return true;
        }
        Toast.makeText(context, R.string.email_not_match, Toast.LENGTH_LONG).show();
        return false;
    }

    private boolean isPasswordMatches(Context context, String password){
        Pattern pattern = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[a-zA-Z]).{6,}$");
        if(pattern.matcher(password).matches()){
            return true;
        }
        Toast.makeText(context, R.string.password_not_match, Toast.LENGTH_LONG).show();
        return false;
    }
}
