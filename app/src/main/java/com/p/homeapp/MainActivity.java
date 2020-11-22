package com.p.homeapp;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.p.homeapp.loginAndRegister.LoginActivity;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {


    private static final int TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityStarter starter = new ActivityStarter();
        starter.start();

    }

    private class ActivityStarter extends Thread {

        @Override
        public void run() {
            try {
                Thread.sleep(TIME_OUT);
            } catch (Exception e) {
                Log.e("Splash Screen", Objects.requireNonNull(e.getMessage()));
            }

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            MainActivity.this.startActivity(intent);
            MainActivity.this.finish();
        }


    }
}
