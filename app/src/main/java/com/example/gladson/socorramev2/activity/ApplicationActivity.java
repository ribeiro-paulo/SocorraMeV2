package com.example.gladson.socorramev2.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.gladson.socorramev2.R;
import com.example.gladson.socorramev2.config.FirebaseConfig;
import com.example.gladson.socorramev2.fragment.RequestHelpFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ApplicationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private BroadcastReceiver broadcastReceiver;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        // Envia um BroadCast para a Activity anterior
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("loginFilter"));
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("mainFilter"));

        // BroadCast para finalizar esta activity.
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        };

        // Adiciona o filtro ao BroadCast.
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("applicationFilter"));

        // Mantém a autenticação do FirebaseAuth e Configura a interface com base nas informações.
        auth = FirebaseConfig.getFirebaseAuth();


        // Configura a Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Socorra-me");

        // Adiciona o Fragment padrão.
        RequestHelpFragment requestHelpFragment = new RequestHelpFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frameLayout, requestHelpFragment);
        transaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Configura o NavigationView com informaçõe do usuário.
        NavigationView navigationView = findViewById(R.id.nav_view);

        FirebaseUser user = auth.getCurrentUser();

        String email = user.getEmail();

        View view = navigationView.getHeaderView(0);
        TextView nav_user = view.findViewById(R.id.textViewUsername);
        TextView nav_user_email = view.findViewById(R.id.textViewUserEmail);
        nav_user.setText("Socorra-me");
        nav_user_email.setText(email);

        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.application, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // TODO MENU DE CONFIGURAÇÕES PARA O COMPORTAMENTO DO APP
            return true;
        } else if (id == R.id.action_about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_contacts) {
            // TODO CRIAR UMA ABA DE CONTATOS DE EMERGÊNCIA
        } else if (id == R.id.nav_request_help) {
            RequestHelpFragment requestHelpFragment = new RequestHelpFragment();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayout, requestHelpFragment);
            transaction.commit();
        } else if (id == R.id.nav_change_account) {
            auth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
        } else if (id == R.id.nav_help) {
            startActivity(new Intent(this, HelpActivity.class));
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(this, AboutActivity.class));
        } else if (id == R.id.nav_exit) {
            this.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Remove o BroadCast caso a activity tenha sido destruida.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }
}
