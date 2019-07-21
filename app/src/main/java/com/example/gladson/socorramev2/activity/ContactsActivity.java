package com.example.gladson.socorramev2.activity;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.gladson.socorramev2.R;
import com.example.gladson.socorramev2.adapter.ContactAdapter;
import com.example.gladson.socorramev2.helper.EmmergencyContactDAO;
import com.example.gladson.socorramev2.helper.RecyclerItemClickListener;
import com.example.gladson.socorramev2.model.EmmergencyContact;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Activity por exibir todos os contatos do usuário para que ele possa selecionar
 * aqueles que serão seus contatos de emergência.
 */
public class ContactsActivity extends AppCompatActivity {

    private MaterialSearchView searchView;
    private RecyclerView recyclerView;
    private ContactAdapter adapter;
    private ArrayList<EmmergencyContact> contacts = new ArrayList<>();
    private ArrayList<EmmergencyContact> viewList = new ArrayList<>();
    private EmmergencyContact selectedContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Configura a ActionBar.
        getSupportActionBar().setTitle("Adicionar contatos de emergência.");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Configurações Iniciais.
        recyclerView = findViewById(R.id.recyclerViewAllContacts);

        // Adicionando evento de click.
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                // Obtém o índice.
                                selectedContact = viewList.get(position);

                                // Cria o AlertDialog.
                                AlertDialog.Builder dialog = new AlertDialog.Builder(ContactsActivity.this);
                                dialog.setTitle("Adicionar contato");
                                dialog.setMessage("Deseja adicionar o contato: " + selectedContact.getName() + "\n" +
                                        "à sua lista de contatos de emergência?");
                                dialog.setCancelable(false);

                                // Botão de negação.
                                dialog.setNegativeButton("Não", null);

                                // Botão de confirmar.
                                dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        EmmergencyContactDAO ecDao = new EmmergencyContactDAO(getApplicationContext());

                                        if (ecDao.save(selectedContact)) {
                                            Toast.makeText(getApplicationContext(),
                                                    "Contato adicionado à lista.",
                                                    Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(),
                                                    "Não foi possível adicionar o contato à lista.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                dialog.create().show();
                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }));


        // Configuração do Adapter.
        adapter = new ContactAdapter(contacts, this);

        // Obtém os contatos do usuário e adiciona no RecyclerView.
        initRecyclerView();

        // Configuração do RecyclerView.
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        searchView = findViewById(R.id.search_view);
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                reload();
            }
        });
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                search(newText.toLowerCase());

                return true;
            }
        });


    }

    public void reload() {
        ContactAdapter adapter = new ContactAdapter(contacts, this);
        recyclerView.setAdapter(adapter);
        viewList = contacts;
        adapter.notifyDataSetChanged();
    }

    public void search(String text) {
        ArrayList<EmmergencyContact> contactsSearch = new ArrayList<>();

        for (EmmergencyContact ec : contacts) {
            String name = ec.getName().toLowerCase();
            String number = ec.getNumber();

            if (name.contains(text) || number.contains(text)) {
                contactsSearch.add(ec);
            }
        }

        ContactAdapter adapter = new ContactAdapter(contactsSearch, this);
        recyclerView.setAdapter(adapter);
        viewList = contactsSearch;
        adapter.notifyDataSetChanged();
    }

    /**
     * Inicializa o RecyclerView com a informação de todos os contatos do usuário.
     */
    public void initRecyclerView() {
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);

                    // Adiciona os números de telefone do contato para um ArrayList
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));

                        phoneNo = phoneNo.replace("-", "");

                        int start = phoneNo.length() - 8;
                        String phoneNumber = "9" + phoneNo.substring(start, start + 4) + "-" + phoneNo.substring(start + 4);

                        EmmergencyContact contact = new EmmergencyContact();
                        contact.setName(name);
                        contact.setNumber(phoneNumber);

                        if (contacts.size() == 0) {
                            contacts.add(contact);
                        } else if (!contacts.get(contacts.size() - 1).getName().equals(contact.getName())) {
                            contacts.add(contact);
                        }
                    }
                    pCur.close();
                }
            }
        }

        if (cur != null) {
            cur.close();
        }

        Collections.sort(contacts);
        viewList = contacts;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        // Configura o botão de pesquisa.
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return super.onCreateOptionsMenu(menu);
    }
}
