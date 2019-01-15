package com.example.gladson.socorramev2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.gladson.socorramev2.R;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.app.SlideFragment;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

/**
 * Na classe principal, estarão as verificações e configurações iniciais dos arquivos.
 */
public class MainActivity extends IntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO PERMIÇÕES VIRÃO AQUI



        // TODO ENTRE ESSES DOIS COMENTÁRIOS

        // Desabilita a exibição dos botões de avançar e voltar.
        setButtonNextVisible(false);
        setButtonBackVisible(false);

        // Slider de introdução.
        addSlide(new FragmentSlide.Builder()
                .background(R.color.mi_icon_color_dark)
                .fragment(R.layout.intro_main)
                .build());

        // Slider sobre a polícia.
        addSlide(new FragmentSlide.Builder()
            .background(R.color.background_policeman)
            .fragment(R.layout.intro_police)
            .build());

        // Slider sobre o corpo de bombeiros.
        addSlide(new FragmentSlide.Builder()
            .background(R.color.background_orange)
            .fragment(R.layout.intro_fireman)
            .build());

        // Slider sobre a ambulância.
        addSlide(new FragmentSlide.Builder()
                .background(R.color.background_red)
                .fragment(R.layout.intro_ambulance)
                .build());

        // Slider que encaminhará para a tela de login.
        addSlide(new FragmentSlide.Builder()
            .background(R.color.mi_icon_color_dark)
            .fragment(R.layout.intro_login)
            .canGoForward(false)
            .build());
    }

    /**
     * Essa classe deve ser atribuida aos botões de "Avançar" nos fragments da INTRO
     *
     * @param view
     */
    public void onAdvanceButtonClicked(View view) {
        nextSlide();
    }

    /**
     * Classe que operará o último slider que encaminhará para a tela de login.
     *
     * @param view
     */
    public void onAppContinueClicked(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }
}
