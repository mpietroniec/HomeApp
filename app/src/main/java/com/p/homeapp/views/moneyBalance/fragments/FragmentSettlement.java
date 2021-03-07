package com.p.homeapp.views.moneyBalance.fragments;

import android.os.Bundle;
import android.os.Parcel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.p.homeapp.R;
import com.p.homeapp.entities.Expenditure;
import com.p.homeapp.entities.Group;

public class FragmentSettlement extends Fragment {
    private TextView settlementGroupName;
    Group group;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_settlement, container, false);
        settlementGroupName = view.findViewById(R.id.txt_fragment_settlement_group_name);
//     Intent intent = getContext().
//        Group group = grt
        group = new Group(Parcel.obtain());
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            group = bundle.getParcelable("Group");
            String groupName = group.getName();
            settlementGroupName.setText(groupName);
       }
       // String groupName = group.getName();

        return view;
    }
}
