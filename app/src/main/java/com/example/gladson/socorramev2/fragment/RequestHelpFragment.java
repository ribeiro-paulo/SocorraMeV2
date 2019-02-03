package com.example.gladson.socorramev2.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.gladson.socorramev2.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestHelpFragment extends Fragment {

    private Button buttonCallPolice;
    private Button buttonCallFireman;
    private Button buttonCallAmbulance;
    private Button buttonCallContact;

    private LocationManager locationManager;
    private LocationListener locationListener;

    public RequestHelpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_help, container, false);

        // Configura os botões do fragment.
        buttonCallPolice = view.findViewById(R.id.buttonCallPolice);
        buttonCallFireman = view.findViewById(R.id.buttonCallFireman);
        buttonCallAmbulance = view.findViewById(R.id.buttonCallAmbulance);
        buttonCallContact = view.findViewById(R.id.buttonCallContact);

        // Configura os listeners dos botões.
        buttonCallPolice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO CHAMADA PARA A POLÍCIA
            }
        });

        buttonCallFireman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO CHAMADA PARA OS BOMBEIROS
            }
        });

        buttonCallAmbulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO CHAMADA PARA A AMBULÂNCIA
            }
        });

        buttonCallContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO CHAMADA PARA OS CONTATOS DE EMERGÊNCIA
            }
        });

        return view;
    }

    /**
     * Este método serve para gerenciar a localização do Usuário.
     */
    private void obtainUserLocation() {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("Localização", "onLocationChanged: " + location.toString());
                Log.d("Localização", "Latitude" + location.getLatitude());
                Log.d("Localização", "Latitude" + location.getLongitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
        }
    }

    /**
     * Método responsável por enviar a todos os contados de emergência do usuário, uma mensagem de socorro.
     */
    private void sendSMSMessage() {
        String number = "(00) 00000-0000";
        String message = "DefaultMsg";

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, null, message, null, null);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Não foi possível avisar um ou mais de seus contatos.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

}
