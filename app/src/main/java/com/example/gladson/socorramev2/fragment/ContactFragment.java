package com.example.gladson.socorramev2.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gladson.socorramev2.R;
import com.example.gladson.socorramev2.activity.ContactsActivity;
import com.example.gladson.socorramev2.adapter.ContactAdapter;
import com.example.gladson.socorramev2.helper.EmmergencyContactDAO;
import com.example.gladson.socorramev2.helper.RecyclerItemClickListener;
import com.example.gladson.socorramev2.model.EmmergencyContact;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment {

    private RecyclerView recyclerView;
    private ContactAdapter adapter;
    private ArrayList<EmmergencyContact> contacts = new ArrayList<>();

    private Button buttonAddContact;
    private TextView textView;

    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        // Configurações Iniciais.
        recyclerView = view.findViewById(R.id.recyclerViewContactList);
        textView = view.findViewById(R.id.textViewContacts);
        buttonAddContact = view.findViewById(R.id.buttonAddContact);

        buttonAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ContactsActivity.class));
            }
        });

        loadContactList();

        // Adiciona o evento de Click ao RecyclerView.
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                // Obtém o índice.
                                final int i = position;

                                // Cria o AlertDialog.
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("Remover contato");
                                builder.setMessage("Deseja remover o contato" + "\n" +
                                        "da sua lista de contatos de emergência?");
                                builder.setCancelable(false);

                                // Botão de negação.
                                builder.setNegativeButton("Não", null);

                                // Botão de confirmar.
                                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        EmmergencyContact ec = contacts.get(i);

                                        EmmergencyContactDAO ecDao = new EmmergencyContactDAO(getActivity());

                                        if (ecDao.delete(ec)) {
                                            loadContactList();
                                            Toast.makeText(getActivity(),
                                                    "Contato removido com sucesso.",
                                                    Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getActivity(),
                                                    "Erro ao remover contato.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadContactList();
    }

    public void loadContactList() {
        // Obtém a lista de contatos de emergência.
        EmmergencyContactDAO ecDao = new EmmergencyContactDAO(getActivity());
        contacts = (ArrayList<EmmergencyContact>) ecDao.list();

        // Configuração do Adapter.
        adapter = new ContactAdapter(contacts, getActivity());

        // Configuração do RecyclerView.
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        if (contacts.size() == 0) {
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

}
