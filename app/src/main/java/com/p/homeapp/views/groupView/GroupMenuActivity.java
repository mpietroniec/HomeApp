package com.p.homeapp.views.groupView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.p.homeapp.R;

public class GroupMenuActivity extends AppCompatActivity {
    private EditText groupNameUpdate;
    private Button findUserByLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_menu);

        groupNameUpdate = findViewById(R.id.group_name_update_etxt);
        findUserByLogin = findViewById(R.id.find_user_by_login_btn);
        findUserByLogin.setOnClickListener(v -> {
            openDialog();
        });
    }

    public void openDialog() {
        GroupMenuDialog groupMenuDialog = new GroupMenuDialog();
        groupMenuDialog.show(getSupportFragmentManager(), "Group dialog");
    }
}