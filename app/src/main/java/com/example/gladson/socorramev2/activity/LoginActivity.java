package com.example.gladson.socorramev2.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.gladson.socorramev2.R;

public class LoginActivity extends AppCompatActivity {


    private Button buttonEnter;
    private Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        buttonEnter = findViewById(R.id.buttonEnter);
        buttonRegister = findViewById(R.id.buttonRegister);

        buttonEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO VERIFICAR OS CAMPOS
                startActivity(new Intent(getApplicationContext(), ApplicationActivity.class));
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
    }
}
