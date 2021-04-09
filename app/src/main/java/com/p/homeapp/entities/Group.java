package com.p.homeapp.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.IgnoreExtraProperties;

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

    private List<String> tasksId;


    @Override
    public String toString() {
        return name;
    }
}




