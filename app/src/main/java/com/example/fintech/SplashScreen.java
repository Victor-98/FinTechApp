package com.example.fintech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        // Timer de Delay para inicialização
        new Handler().postDelayed(
            new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashScreen.this, Dashboard.class));
                    finish();
                }
            }, 3000);
    }
}