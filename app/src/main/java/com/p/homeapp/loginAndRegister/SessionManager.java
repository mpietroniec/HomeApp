package com.p.homeapp.loginAndRegister;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences usersSession;
    SharedPreferences.Editor editor;
    Context mContext;

    //Session names
    public static final String SESSION_USERSESSION = "userLoginSession";
    public static final String SESSION_REMEMBER_ME = "rememberMe";

    //User session variables
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_MAIL = "mail";
    private static final String KEY_PASSWORD = "password";

    //Remember Me variables
    private static final String IS_REMEMBER_ME = "IsRememberIn";
    private static final String KEY_SESSION_MAIL = "mail";
    private static final String KEY_SESSION_PASSWORD = "password";

    public SessionManager(Context context, String sessionName) {
        mContext = context;
        usersSession = context.getSharedPreferences(sessionName, Context.MODE_PRIVATE);
        editor = usersSession.edit();
    }

    public void createRememberMeSession(String mail, String password) {

        editor.putBoolean(IS_REMEMBER_ME, true);

        editor.putString(KEY_SESSION_MAIL, mail);
        editor.putString(KEY_PASSWORD, password);

        editor.commit();
    }

    public HashMap<String, String> getRememberMeDetailsFromSession(){
        HashMap<String,String> userData = new HashMap<>();

        userData.put(KEY_SESSION_MAIL, usersSession.getString(KEY_SESSION_MAIL,null));
        userData.put(KEY_SESSION_PASSWORD, usersSession.getString(KEY_SESSION_PASSWORD,null));

        return userData;
    }

    public boolean checkRememberMe(){
        if (usersSession.getBoolean(IS_REMEMBER_ME,false)){
            return true;
        }else {
            return false;
        }
    }
}
