package com.example.gladson.socorramev2.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gladson.socorramev2.R;
import com.example.gladson.socorramev2.config.FirebaseConfig;
import com.example.gladson.socorramev2.fragment.LoadingAlertFragment;
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

    private Button buttonEnter;
    private Button buttonRegister;

    private TextInputEditText editEmail;
    private TextInputEditText editPassword;

    private DialogFragment dialogFragment;

    private TextView textViewPasswordReset;

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

        editEmail = findViewById(R.id.editLoginEmail);
        editPassword = findViewById(R.id.editLoginPassword);

        buttonEnter = findViewById(R.id.buttonEnter);
        buttonRegister = findViewById(R.id.buttonRegister);

        textViewPasswordReset = findViewById(R.id.textResetPassword);

    }

    public void onButtonRegisterClicked(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void onTextResetPasswordClicked(View view) {

        final String email = editEmail.getText().toString();

        if (email != null && !email.equals("")) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Recuperar Senha");
            builder.setMessage("Deseja enviar um email para a recuperação de senha?");
            builder.setCancelable(false);
            builder.setNegativeButton("Não", null);
            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    auth.sendPasswordResetEmail(editEmail.getText().toString());

                    Toast.makeText(LoginActivity.this, "Email de recuperação enviado para: " + email, Toast.LENGTH_SHORT).show();
                }
            });

            builder.create().show();

        }
    }

    public void onButtonEnterClicked(View view) {

        // Inicializa o DialogFragment.
        dialogFragment = new LoadingAlertFragment();
        dialogFragment.setCancelable(false);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Variável de validação.
        boolean valid = true;

        // Recupera dados do EditText.
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();

        // Verificação dos campos.
        if (email.isEmpty()) {
            editEmail.setError("Email inválido!");
            valid = false;
        }

        if (password.isEmpty()) {
            editPassword.setError("Senha inválida!");
            valid = false;
        }

        if (valid) {
            // Exibe o DialogFragment e Valida o Login.
            dialogFragment.show(transaction, "");
            validateFirebaseLogin(email, password);
        }

    }

    public void validateFirebaseLogin(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // Desmonta o DialogFragment.
                dialogFragment.dismiss();

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
