package com.p.homeapp.helpers;

import android.content.Context;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.p.homeapp.DB.DBHelper;
import com.p.homeapp.entities.User;

import java.util.regex.Pattern;

public class AccountDataValidator extends AppCompatActivity {



    public boolean validateLoginData(User user, String password, Context context){
        if(isUserExist(user, context)){
            if(isPasswordCorrect(user, password, context) && isUserExist(user, context)){
                Toast.makeText(context, "Login successful", Toast.LENGTH_LONG).show();
                return true;
            }
        }
        return false;
    }

    private boolean isUserExist(User user, Context context){
        if (user.getId() != 0 ) {
            return true;
        }
        Toast.makeText(context, "User doesn't exist", Toast.LENGTH_LONG).show();
        return false;
    }

    private boolean isPasswordCorrect(User user, String password, Context context){
        System.out.println("password: " + password.isEmpty());
        if(password.isEmpty()){
            Toast.makeText(context, "Password field is empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if(BCryptHelper.checkPassword(password, user.getPassword())){
            return true;
        }
        Toast.makeText(context, "Password incorrect", Toast.LENGTH_LONG).show();
        return false;
    }

    public boolean validateRegisterData(Context context, String password, String confirmPassword, User user, DBHelper dbHelper){
        if(isPasswordConfirmed(context, password, confirmPassword) &&
                isRegisterFieldsEmpty(context, user.getLogin(), user.getEmail(), password) &&
                isLoginExist(context, user.getLogin(), dbHelper) &&
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
        Toast.makeText(context, "Confirmed password incorrect", Toast.LENGTH_LONG).show();
        return false;
    }

    private boolean isLoginExist(Context context, String login, DBHelper dbHelper){
        User user = dbHelper.getUser(login);
        if(user.getId() == 0){
            return true;
        }

        Toast.makeText(context, "Username already exist", Toast.LENGTH_LONG).show();
        return false;
    }

    private boolean isRegisterFieldsEmpty(Context context, String login, String email, String password){
        if(login == null || login.isEmpty() || login.equals("")){
            Toast.makeText(context, "Login field is empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if(email == null || email.isEmpty() || email.equals("")){
            Toast.makeText(context, "Email field is empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if(password == null || password.isEmpty() || password.equals("")){
            Toast.makeText(context, "Password field is empty", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean isLoginMatches(Context context, String login){
        Pattern pattern = Pattern.compile("^(?=.*[A-Za-z0-9]$)[A-Za-z][A-Za-z\\d.-]{0,19}$");
        if (pattern.matcher(login).matches()){
            return true;
        }
        Toast.makeText(context, "Login must start with a letter, end with a letter or digit and must be shorter than 19", Toast.LENGTH_LONG).show();
        return false;
    }

    private boolean isEmailMatches(Context context, String email){
        Pattern pattern = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?");
        if(pattern.matcher(email).matches()){
            return true;
        }
        Toast.makeText(context, "Email is incorrect", Toast.LENGTH_LONG).show();
        return false;
    }

    private boolean isPasswordMatches(Context context, String password){
        Pattern pattern = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[a-zA-Z]).{6,}$");
        if(pattern.matcher(password).matches()){
            return true;
        }
        Toast.makeText(context, "Password must have one letter, one digit and can't be shorter than 6 characters", Toast.LENGTH_LONG).show();
        return false;
    }
}
