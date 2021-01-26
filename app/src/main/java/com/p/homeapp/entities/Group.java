package com.p.homeapp.entities;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Group {

    private String id;
    private String name;
    private String description;
    private User creator;
    private List<User> members;
    private List<Task> tasks;
}
