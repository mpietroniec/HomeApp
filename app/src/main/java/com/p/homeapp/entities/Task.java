package com.p.homeapp.entities;

import android.graphics.drawable.Drawable;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    private long taskID;
    private String taskName;
    private LocalDateTime deadline;
    private int drawable;

    @Override
    public String toString() {
        return "Task{" +
                "taskID=" + taskID +
                ", taskName='" + taskName + '\'' +
                '}';
    }
}
