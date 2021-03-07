package com.p.homeapp.views.mainView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.p.homeapp.MainActivity;
import com.p.homeapp.R;
import com.p.homeapp.entities.User;
import com.p.homeapp.views.archives.TaskArchivesActivity;
import com.p.homeapp.views.groupView.GroupActivity;
import com.p.homeapp.views.invitation.InvitationMenuActivity;

public class FragmentActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView navLogin, navMail;
    private  BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        navigationView = findViewById(R.id.nv_header);
        View view = navigationView.getHeaderView(0);
        navLogin = view.findViewById(R.id.txt_login_header);
        getUserLogin();
        navMail = view.findViewById(R.id.txt_email_header);
        navMail.setText(getUserMail());

        bottomNavigationView = findViewById(R.id.id_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.id_fragment_container, new FragmentTasks()).commit();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.id_nav_task:
                        selectedFragment = new FragmentTasks();
                        break;
                    case R.id.id_nav_money_balance:
                        selectedFragment = new FragmentMoneyBalance();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.id_fragment_container,
                        selectedFragment).commit();
                return true;
            };

    public void startArchivesActivity(MenuItem item) {
            startActivity(new Intent(FragmentActivity.this, TaskArchivesActivity.class));
    }

    public void logout(MenuItem item) {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(FragmentActivity.this, R.string.logged_out, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(FragmentActivity.this, MainActivity.class));
        finish();
    }

    public void startGroupActivity(MenuItem item) {
        Intent intent = new Intent(FragmentActivity.this, GroupActivity.class);
        startActivityForResult(intent, 1);
        drawerLayout.closeDrawers();
    }

    public void startInvitationActivity(MenuItem item) {
        Intent intent = new Intent(FragmentActivity.this, InvitationMenuActivity.class);
        startActivity(intent);
        drawerLayout.closeDrawers();
    }

    public void getUserLogin() {
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase.getInstance().getReference().child("users").child(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    User user = dataSnapshot.getValue(User.class);
                    navLogin.setText(user.getLogin());
                }
            }
        });
    }

    public String getUserMail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user.getEmail();
    }

}
