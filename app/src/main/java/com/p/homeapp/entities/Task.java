package com.p.homeapp.entities;

import android.graphics.drawable.Drawable;

import com.google.firebase.database.Exclude;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    private String id;
    private String taskName;
    private Date deadline;
    private String taskType;
    private boolean taskNotification;
    private int drawable;
    private User user;

    @Override
    public String toString() {
        return "Task{" +
                "taskID=" + id +
                ", taskName='" + taskName + '\'' +
                '}';
    }
}
