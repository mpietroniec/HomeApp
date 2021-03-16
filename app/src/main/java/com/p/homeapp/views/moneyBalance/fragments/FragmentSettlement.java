package com.p.homeapp.views.moneyBalance.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.p.homeapp.R;

public class FragmentSettlement extends Fragment {
    private TextView settlementGroupName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_settlement, container, false);
        settlementGroupName = view.findViewById(R.id.txt_fragment_settlement_group_name);

        return view;
    }
}
