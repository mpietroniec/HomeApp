package com.p.homeapp.entities;


import java.time.LocalDateTime;


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
}


