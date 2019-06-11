package com.example.gladson.socorramev2.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.example.gladson.socorramev2.R;
import com.github.zagum.switchicon.SwitchIconView;

import java.io.IOException;

public class SendMediaActivity extends AppCompatActivity {

    private SwitchIconView switchIconViewRecord;
    private View buttonRecord;
    private MediaRecorder recorder = null;
    private String fileName = null;

    private Button buttonSend;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_media);

        getSupportActionBar().setTitle("Enviar Mídia");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        // Aleatoriza o nome do Arquivo
        fileName = getExternalCacheDir().getAbsolutePath();
        fileName += "/socorrame.3gp";

        switchIconViewRecord = findViewById(R.id.switchIconViewRecord);

        buttonSend = findViewById(R.id.buttonSend);
        buttonSend.setEnabled(false);

        buttonRecord = findViewById(R.id.buttonRecord);
        buttonRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchIconViewRecord.switchState();

                if (switchIconViewRecord.isIconEnabled()) {
                    startRecording();
                } else {
                    Toast.makeText(SendMediaActivity.this, "Parando...", Toast.LENGTH_SHORT).show();
                    stopRecording();
                    buttonSend.setEnabled(true);
                }

            }
        });
    }

    /**
     * Inicia a gravação
     */
    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            Toast.makeText(SendMediaActivity.this, "Gravando...", Toast.LENGTH_SHORT).show();
            recorder.prepare();
        } catch (IOException e) {
            Log.e("AudioRecorder", "prepare() failed");
        }

        recorder.start();
    }

    /**
     * Para a gravação
     */
    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        this.finish();
        return super.onSupportNavigateUp();
    }

}
