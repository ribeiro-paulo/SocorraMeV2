package com.example.gladson.socorramev2.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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
import com.example.gladson.socorramev2.helper.EmmergencyContactDAO;
import com.example.gladson.socorramev2.model.EmmergencyContact;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    private double latitude;
    private double longitude;
    private String onLocationChanged;
    private String addressLine;

    private ArrayList<EmmergencyContact> contacts = new ArrayList<>();

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

        obtainUserLocation();

        // Configura os listeners dos botões.
        buttonCallPolice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO CHAMADA PARA A POLÍCIA
                getAddressLocation();
                showUserLocation();
            }
        });

        buttonCallFireman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO CHAMADA PARA OS BOMBEIROS
                getAddressLocation();
                showUserLocation();
            }
        });

        buttonCallAmbulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO CHAMADA PARA A AMBULÂNCIA
                getAddressLocation();
                showUserLocation();
            }
        });

        buttonCallContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO ENVIAR MENSAGEM
                getAddressLocation();
                sendSMSMessage();
            }
        });

        // Retorno da View.
        return view;
    }

    /**
     * Obtém a latitude e longitude e converte para um endereço legível em linguagem natural.
     */
    private void getAddressLocation() {

        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);

            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                addressLine = address.getAddressLine(0);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Este método serve para iniciar e gerenciar a localização do Usuário.
     */
    private void obtainUserLocation() {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                onLocationChanged = location.toString();
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                /**
                 * Logs para debug.
                 */
                Log.d("Localização", "onLocationChanged: " + onLocationChanged);
                Log.d("Localização", "Latitude: " + latitude);
                Log.d("Localização", "Longitude: " + longitude);
                Log.d("Localização", "AddressLine: " + addressLine);
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

        // Verifica as permições.
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
        }
    }

    /**
     * TODO Método temporário.
     */
    private void showUserLocation() {

        String message = "Latitude: " + latitude + "\n" +
                            "Longitude: " + longitude + "\n" +
                            "Endereço: " + addressLine;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Localização");
        builder.setMessage(message);
        builder.create().show();

    }

    /**
     * // TODO NÃO FUNCIONANDO CORRETAMENTE
     * Método responsável por enviar a todos os contados de emergência do usuário, uma mensagem de socorro.
     */
    private void sendSMSMessage() {

        EmmergencyContactDAO ecDao = new EmmergencyContactDAO(getActivity());
        contacts = (ArrayList<EmmergencyContact>) ecDao.list();


        String message = "Socorro!!!";
                /* "Me Ajude! Estou em alguma situação de perigou ou precisando de ajuda!\n";+
                "Minha localização é: " + "\n" +
                "Latitude: " + latitude + "\n" +
                "Longitude: " + longitude + "\n" +
                "Endereço: " + addressLine;*/

        // TODO Mover este trecho de código para uma Thread.
        for (EmmergencyContact c : contacts) {
            String number = c.getNumber();

            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(number, null, message, null, null);

                // Verifica o tamanho do endereço para enviar.
                if (addressLine.length() < 150) {
                    smsManager.sendTextMessage(number, null, addressLine, null, null);
                } else {
                    smsManager.sendTextMessage(number, null, addressLine.substring(0, 149), null, null);
                    smsManager.sendTextMessage(number, null, addressLine.substring(150), null, null);
                }

                Toast.makeText(getActivity(), "Mensagem enviada para: "  + number, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Não foi possível avisar um ou mais de seus contatos.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

    }

}
