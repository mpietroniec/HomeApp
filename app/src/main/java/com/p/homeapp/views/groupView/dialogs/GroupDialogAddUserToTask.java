package com.p.homeapp.views.groupView.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;


public class GroupDialogAddUserToTask extends AppCompatDialogFragment {

    private String groupId;
    private Context mContext;

    public GroupDialogAddUserToTask(String groupId, Context mContext) {
        this.groupId = groupId;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable  Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }
}