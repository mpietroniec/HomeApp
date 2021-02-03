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
    public String name;
    public String description;
    private String creatorUserId;
    private List<String> membersId;
    private List<String> tasksId;
}
