package com.example.gladson.socorramev2.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.gladson.socorramev2.R;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setTitle("Nova conta");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onRegisterButtonClick(View view) {
        // TODO VERIFICAÇÃO PARA O LOGIN
    }

    @Override
    public boolean onSupportNavigateUp() {
        this.finish();
        return super.onSupportNavigateUp();
    }
}
