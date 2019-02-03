package com.example.gladson.socorramev2.activity;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.gladson.socorramev2.R;
import com.example.gladson.socorramev2.config.FirebaseConfig;
import com.example.gladson.socorramev2.fragment.LoadingFragment;
import com.example.gladson.socorramev2.fragment.RegisterFragment;
import com.example.gladson.socorramev2.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

/**
 *  Activity responsável por realizar o cadastro do usuário.
 */
public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private RegisterFragment registerFragment = new RegisterFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setTitle("Nova conta");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frameLayoutRegister, registerFragment);
        transaction.commit();

    }

    public void registerUserInFirebase(User user) {

        LoadingFragment loadingFragment = new LoadingFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayoutRegister, loadingFragment);
        transaction.commit();

        auth = FirebaseConfig.getFirebaseAuth();
        auth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frameLayoutRegister, registerFragment);
                        transaction.commit();

                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this,
                                    "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                Toast.makeText(RegisterActivity.this,
                                        "Senha muito fraca.", Toast.LENGTH_SHORT).show();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(RegisterActivity.this, "Email inválido",
                                        Toast.LENGTH_SHORT).show();
                            } catch (FirebaseAuthUserCollisionException e) {
                                Toast.makeText(RegisterActivity.this, "Usuário já cadastrado",
                                        Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(RegisterActivity.this,
                                        "Erro ao cadastrar usuário: " + e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        this.finish();
        return super.onSupportNavigateUp();
    }
}
