package com.p.homeapp.entities;

import java.time.LocalDateTime;

import at.favre.lib.crypto.bcrypt.BCrypt;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private long id;
    private String login;
    private String email;
    private String password;

    private LocalDateTime createDate;
    private String role;

    public User(long id, String login, String email, String password, LocalDateTime createDate) {

        this.id = id;
        this.login = login;
        this.email = email;
        this.password = password;
        this.createDate = LocalDateTime.now();
        this.role = "ROLE_USER";
    }

    public void setPassword(String password) {
        String hashed = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        this.password = password;
    }

    public boolean checkPassword(String password){
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), this.password);
        System.out.println(password + " " + this.password + " " + result.verified);
        return result.verified;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", createDate=" + createDate +
                ", role='" + role + '\'' +
                '}';
    }
}


