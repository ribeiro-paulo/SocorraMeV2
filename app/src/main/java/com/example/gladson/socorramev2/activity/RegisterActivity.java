package com.example.gladson.socorramev2.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.gladson.socorramev2.R;
import com.example.gladson.socorramev2.config.FirebaseConfig;
import com.example.gladson.socorramev2.fragment.LoadingAlertFragment;
import com.example.gladson.socorramev2.helper.CpfCnpjMaks;
import com.example.gladson.socorramev2.helper.Validator;
import com.example.gladson.socorramev2.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

/**
 * Activity responsável por realizar o cadastro do usuário.
 */
public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private TextInputEditText editName;
    private TextInputEditText editEmail;
    private TextInputEditText editCPF;
    private TextInputEditText editPassword;
    private TextInputEditText editConfirmPassword;

    private Button button;

    private DialogFragment dialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setTitle("Nova conta");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Configuração dos TextInputEditText.
        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editCPF = findViewById(R.id.editCPF);
        editCPF.addTextChangedListener(CpfCnpjMaks.insert(editCPF));
        editPassword = findViewById(R.id.editPassword);
        editConfirmPassword = findViewById(R.id.editConfirmPassword);

        // Configuração do botão.
        button = findViewById(R.id.button);

    }

    public void registerUserInFirebase(View view) {

        // Recupera os dados dos EditText.
        String name = editName.getText().toString();
        String email = editEmail.getText().toString();
        String cpf = editCPF.getText().toString();
        String password = editPassword.getText().toString();
        String confimPassword = editConfirmPassword.getText().toString();

        // Variável de controle.
        boolean valid = true;

        // Informa que os dados informados estão incorretos ou ausentes.
        if (name.isEmpty()) {
            editName.setError("Campo obrigatório!");
            valid = false;
        }

        if (email.isEmpty()) {
            editEmail.setError("Campo obrigatório!");
            valid = false;
        }

        if (cpf.isEmpty()) {
            editCPF.setError("Campo obrigatório!");
            valid = false;
        }

        if (!Validator.isValidCPF(cpf)) {
            editCPF.setError("Entre com um CPF válido!");
            valid = false;
        }

        if (password.isEmpty()) {
            editPassword.setError("Campo obrigatório");
            valid = false;
        }
        if (confimPassword.isEmpty()) {
            editConfirmPassword.setError("Campo obrigatório!");
            valid = false;
        }

        if (!password.equals(confimPassword) && !password.isEmpty() && !confimPassword.isEmpty()) {
            editPassword.setError("As senhas não coincidem!");
            editConfirmPassword.setError("As senhas não coincidem!");
            valid = false;
        }

        // Inicializa o DialogFragment.
        dialogFragment = new LoadingAlertFragment();
        dialogFragment.setCancelable(false);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Realiza o cadastro caso os dados estejam corretos.
        if (valid) {

            dialogFragment.show(transaction, "");

            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            user.setCpf(cpf);

            auth = FirebaseConfig.getFirebaseAuth();
            auth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            dialogFragment.dismiss();

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

    }

    @Override
    public boolean onSupportNavigateUp() {
        this.finish();
        return super.onSupportNavigateUp();
    }
}
