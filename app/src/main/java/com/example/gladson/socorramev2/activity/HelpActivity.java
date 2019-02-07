package com.example.gladson.socorramev2.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.gladson.socorramev2.R;

// TODO TERMINAR ESTA ACTIVITY
public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        getSupportActionBar().setTitle("Ajuda");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        this.finish();
        return super.onSupportNavigateUp();
    }
}
