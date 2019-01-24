package com.example.gladson.socorramev2.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import mehdi.sakout.aboutpage.AboutPage;
import com.example.gladson.socorramev2.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle("Sobre");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String description = "Este app tem por objetivo auxiliar a requisição de socorro " +
                "para os principais órgãos de segurança pública de maneira rápida " +
                "e eficiente através do GPS do dispositivo.";

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.logo_sc)
                .setDescription(description)
                .addGroup("Contate-nos")
                .addEmail("gladsonsouza@live.com", "Email")
                .addGitHub("Gladson0101", "Contribua no github")
                .addInstagram("gladson_sza", "Siga-nos no instagram")
                .create();

        setContentView(aboutPage);
    }

    @Override
    public boolean onSupportNavigateUp() {
        this.finish();
        return super.onSupportNavigateUp();
    }
}
