package com.android45.orderdrinks.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.android45.orderdrinks.R;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
    }

    public void Register(View view){
        startActivity(new Intent(WelcomeActivity.this, SignUpActivity.class));
    }

    public void Signin(View view) {
        startActivity(new Intent(WelcomeActivity.this, SignInActivity.class));
    }
}