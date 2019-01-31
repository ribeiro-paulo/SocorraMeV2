package com.example.gladson.socorramev2.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.gladson.socorramev2.R;

public class LoginActivity extends AppCompatActivity {

    private static final String PREFERENCES_FILE = "PreferecesFile";
    private Button buttonEnter;
    private Button buttonRegister;

    private SharedPreferences sp;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        // Envia um BroadCast para a Activity anterior
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("mainFilter"));
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("applicationFilter"));

        // BroadCast para finalizar esta activity.
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        };

        // Adiciona o filtro ao BroadCast.
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("loginFilter"));

        sp = getSharedPreferences("UserStatus", MODE_PRIVATE);

        if (sp.getBoolean("logged", false)) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }

        buttonEnter = findViewById(R.id.buttonEnter);
        buttonRegister = findViewById(R.id.buttonRegister);

        buttonEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO VERIFICAR OS CAMPOS
                // Mantém o usuário logado após o primeiro login.
                sp.edit().putBoolean("logged", true).apply();

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

    @Override
    protected void onDestroy() {
        // Remove o BroadCast caso a activity tenha sido destruida.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);

        super.onDestroy();
    }
}
