package com.example.gladson.socorramev2.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.gladson.socorramev2.R;
import com.example.gladson.socorramev2.config.FirebaseConfig;
import com.example.gladson.socorramev2.fragment.LoadingFragment;
import com.example.gladson.socorramev2.fragment.LoginFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;

/**
 * Activity Responsável pela realização do login do usuário e por redirecionar para a Activity de Cadastro.
 */
public class LoginActivity extends AppCompatActivity {
    private BroadcastReceiver broadcastReceiver;

    private FirebaseAuth auth;

    private LoginFragment loginFragment = new LoginFragment();

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

        // Realiza o login automático do usuário caso ele esteja logado.
        auth = FirebaseConfig.getFirebaseAuth();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frameLayoutLogin, loginFragment);
        transaction.commit();


    }

    public void onButtonEnterClicked() {

        LoadingFragment loadingFragment = new LoadingFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayoutLogin, loadingFragment);
        transaction.commit();

        auth.signInWithEmailAndPassword(loginFragment.getEmail(), loginFragment.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayoutLogin, loginFragment);
                transaction.commit();

                if (task.isSuccessful()) {
                    startActivity(new Intent(getApplicationContext(), ApplicationActivity.class));
                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        Toast.makeText(getApplicationContext(),
                                "Credenciais inválidas!", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(),
                                "Erro ao realizar login: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null)
            startActivity(new Intent(getApplicationContext(), ApplicationActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Remove o BroadCast caso a activity tenha sido destruida.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }
}
