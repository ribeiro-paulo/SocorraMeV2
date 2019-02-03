package com.example.gladson.socorramev2.fragment;


import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.gladson.socorramev2.R;
import com.example.gladson.socorramev2.activity.RegisterActivity;
import com.example.gladson.socorramev2.helper.Validator;
import com.example.gladson.socorramev2.model.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private TextInputEditText editName;
    private TextInputEditText editEmail;
    private TextInputEditText editCPF;
    private TextInputEditText editPassword;
    private TextInputEditText editConfirmPassword;

    private Button button;

    private View view;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_register, container, false);

        // Configuração dos TextInputEditText.
        editName = view.findViewById(R.id.editName);
        editEmail = view.findViewById(R.id.editEmail);
        editCPF = view.findViewById(R.id.editCPF);
        editPassword = view.findViewById(R.id.editPassword);
        editConfirmPassword = view.findViewById(R.id.editConfirmPassword);

        // Configuração do botão.
        button = view.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                // Realiza o cadastro caso os dados estejam corretos.
                if (valid) {
                    User user = new User();
                    user.setName(name);
                    user.setEmail(email);
                    user.setPassword(password);
                    user.setCpf(cpf);

                    ((RegisterActivity) getActivity()).registerUserInFirebase(user);
                }
            }
        });

        return view;

    }

}

