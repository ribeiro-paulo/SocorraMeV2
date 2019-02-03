package com.example.gladson.socorramev2.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.gladson.socorramev2.R;
import com.example.gladson.socorramev2.activity.LoginActivity;
import com.example.gladson.socorramev2.activity.RegisterActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private Button buttonEnter;
    private Button buttonRegister;

    private TextInputEditText editEmail;
    private TextInputEditText editPassword;

    private View view;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);

        editEmail = view.findViewById(R.id.editLoginEmail);
        editPassword = view.findViewById(R.id.editLoginPassword);

        buttonEnter = view.findViewById(R.id.buttonEnter);
        buttonRegister = view.findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RegisterActivity.class));
            }
        });

        buttonEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    ((LoginActivity)getActivity()).onButtonEnterClicked();
                }
            }
        });

        return view;
    }

    public String getEmail() {
        return editEmail.getText().toString();
    }

    public String getPassword() {
        return editPassword.getText().toString();
    }
}
