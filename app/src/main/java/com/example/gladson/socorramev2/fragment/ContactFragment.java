package com.example.gladson.socorramev2.fragment;


import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.gladson.socorramev2.R;
import com.example.gladson.socorramev2.adapter.ContactAdapter;
import com.example.gladson.socorramev2.model.EmmergencyContact;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment {

    private ProgressBar progressBar;

    private RecyclerView recyclerView;
    private ContactAdapter adapter;
    private ArrayList<EmmergencyContact> contacts = new ArrayList<>();

    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        // Configurações Iniciais.
        progressBar = view.findViewById(R.id.progressBarContact);
        recyclerView = view.findViewById(R.id.recyclerViewContactList);

        // Configuração do Adapter.
        adapter = new ContactAdapter(contacts, getActivity());

        // Configuração do RecyclerView.
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        return view;
    }



    private void initContactList() {
        // TODO APENAS CONTATOS DE EMERGÊNCIA
    }
}
