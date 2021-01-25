package com.p.homeapp.views.groupView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;

import com.p.homeapp.R;

public class GroupActivity extends AppCompatActivity {

    private Animation rotateOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
    }
}