package com.p.homeapp.entities;

import java.sql.Timestamp;

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
    private Timestamp createDate;
    private String role;

    public User(long id, String login, String email, String password, Timestamp createDate) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.password = password;
        this.createDate = createDate;
        this.role = "ROLE_USER";
    }
}


