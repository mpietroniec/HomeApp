package com.p.homeapp.entities;

import java.time.LocalDateTime;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCryptFormatter;
import at.favre.lib.crypto.bcrypt.BCryptParser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String id;
    private String login;
    private String email;

    private LocalDateTime createDate;

    public User(String id, String login, String email) {

        this.id = id;
        this.login = login;
        this.email = email;
        this.createDate = LocalDateTime.now();

    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", email='" + email + '\'' +
                ", createDate=" + createDate + '\'' +
                '}';
    }
}


