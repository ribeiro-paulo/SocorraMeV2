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
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.gladson.socorramev2.R;
import com.example.gladson.socorramev2.adapter.ContactAdapter;
import com.example.gladson.socorramev2.helper.EmmergencyContactDAO;
import com.example.gladson.socorramev2.helper.RecyclerItemClickListener;
import com.example.gladson.socorramev2.model.EmmergencyContact;

import java.util.ArrayList;

/**
 * Activity por exibir todos os contatos do usuário para que ele possa selecionar
 * aqueles que serão seus contatos de emergência.
 */
public class ContactsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ContactAdapter adapter;
    private ArrayList<EmmergencyContact> contacts = new ArrayList<>();
    private EmmergencyContact selectedContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

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
                                selectedContact = contacts.get(position);

                                // Cria o AlertDialog.
                                AlertDialog.Builder dialog = new AlertDialog.Builder(ContactsActivity.this);
                                dialog.setTitle("Adicionar contato");
                                dialog.setMessage("Deseja adicionar o contato" + "\n" +
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

    }

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

                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));


                        EmmergencyContact contact = new EmmergencyContact();
                        contact.setName(name);
                        contact.setNumber(phoneNo);

                        contacts.add(contact);
                    }
                    pCur.close();
                }
            }
        }
        if (cur != null) {
            cur.close();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
