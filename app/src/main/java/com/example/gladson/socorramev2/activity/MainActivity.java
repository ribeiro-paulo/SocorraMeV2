package com.example.gladson.socorramev2.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;

import com.example.gladson.socorramev2.R;
import com.example.gladson.socorramev2.helper.Permissions;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.app.SlideFragment;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

/**
 * Na classe principal, estarão as verificações e configurações iniciais dos arquivos.
 */
public class MainActivity extends IntroActivity {

    private static final String PREFERENCES_FILE = "PreferecesFile";
    private String[] permissions = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Valida as permições.
        Permissions.validatePermissions(permissions, this, 1);

        // BroadCast para finalizar esta activity.
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        };

        // Adiciona o filtro ao BroadCast.
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("mainFilter"));

        // Verifica se o app foi inicializado alguma vez e ignora a tela de abertura.
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES_FILE, 0);
        if (sharedPreferences.contains("appLaunched")) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int permissionResult : grantResults) {
            // Se a permição for negada, sai do app.
            if (permissionResult == PackageManager.PERMISSION_DENIED) {
                permissionDeniedMessage();
            }
        }
    }

    /**
     * Cria uma janela informando que a permição foi negada.
     */
    public void permissionDeniedMessage() {
        // Builder para criar a interface de alerta.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissão negada!");
        builder.setMessage("Para utilizar o app corretamente, você deve aceitar as permições.");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        // Cria a mensagem de alerta.
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Classe que operará o último slider que encaminhará para a tela de login.
     *
     * @param view
     */
    public void onAppContinueClicked(View view) {
        // Define que o app foi executado ao menos uma vez.
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES_FILE, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("appLaunched", "yes");
        editor.commit();

        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    protected void onDestroy() {
        // Remove o BroadCast caso a activity tenha sido destruida.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);

        super.onDestroy();
    }
}
