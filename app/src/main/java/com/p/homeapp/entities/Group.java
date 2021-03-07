package com.p.homeapp.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Group implements Parcelable {

    private String id;
    public String name;
    public String description;
    private String creatorUserId;
    private List<String> membersId;
    private List<String> tasksId;

// zmienione z "protected"
    public Group(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        creatorUserId = in.readString();
        membersId = in.createStringArrayList();
        tasksId = in.createStringArrayList();
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(creatorUserId);
        dest.writeStringList(membersId);
        dest.writeStringList(tasksId);
    }
}
