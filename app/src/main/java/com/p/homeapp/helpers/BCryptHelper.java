package com.p.homeapp.helpers;

import com.p.homeapp.entities.User;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class BCryptHelper {

    public static boolean checkPassword(String password, String hashedPassword){
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hashedPassword);
        return result.verified;
    }

    public static void hashPassword(User user, String password) {
        String hashed = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        user.setPassword(hashed);
    }
}
