package com.example.gladson.socorramev2.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gladson.socorramev2.R;
import com.example.gladson.socorramev2.activity.SendMediaActivity;
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

    private ImageView imageViewLocation;
    private TextView textViewLocation;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private double latitude;
    private double longitude;
    private String onLocationChanged;
    private String addressLine;

    private String phoneNumberCall = null;

    private final String NUMERO_POLICIA = "190";
    private final String NUMERO_BOMBEIRO = "193";
    private final String NUMERO_SAMU = "192";


    private ArrayList<EmmergencyContact> contacts = new ArrayList<>();

    public RequestHelpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_help, container, false);

        // Configura os bot??es do fragment.
        buttonCallPolice = view.findViewById(R.id.buttonCallPolice);
        buttonCallFireman = view.findViewById(R.id.buttonCallFireman);
        buttonCallAmbulance = view.findViewById(R.id.buttonCallAmbulance);
        buttonCallContact = view.findViewById(R.id.buttonCallContact);

        imageViewLocation = view.findViewById(R.id.imageViewLocation);
        textViewLocation = view.findViewById(R.id.textViewLocation);

        obtainUserLocation();

        // Configura os listeners dos bot??es.
        buttonCallPolice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO CHAMADA PARA A POL??CIA
                getAddressLocation();

                if (latitude != 0 && longitude != 0 && addressLine != null && !addressLine.isEmpty()) showUserLocation();
                else cantShowUserLocation(NUMERO_POLICIA);

            }
        });

        buttonCallFireman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO CHAMADA PARA OS BOMBEIROS
                getAddressLocation();

                if (latitude != 0 && longitude != 0 && addressLine != null && !addressLine.isEmpty()) showUserLocation();
                else cantShowUserLocation(NUMERO_BOMBEIRO);
            }
        });

        buttonCallAmbulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO CHAMADA PARA A AMBUL??NCIA
                getAddressLocation();

                if (latitude != 0 && longitude != 0 && addressLine != null && !addressLine.isEmpty()) showUserLocation();
                else cantShowUserLocation(NUMERO_SAMU);
            }
        });

        buttonCallContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAddressLocation();

                if (latitude != 0 && longitude != 0 && addressLine != null && !addressLine.isEmpty()) sendSMSMessage();
                else Toast.makeText(getActivity(), "N??o foi poss??vel acessar a sua localiza????o, mensagem n??o enviada", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    /**
     * Obt??m a latitude e longitude e converte para um endere??o leg??vel em linguagem natural.
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
     * Este m??todo serve para iniciar e gerenciar a localiza????o do Usu??rio.
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
                Log.d("Localiza????o", "onLocationChanged: " + onLocationChanged);
                Log.d("Localiza????o", "Latitude: " + latitude);
                Log.d("Localiza????o", "Longitude: " + longitude);
                Log.d("Localiza????o", "AddressLine: " + addressLine);

                /**
                 * Feedback para o usu??rio saber se sua localiza????o foi encontrada.
                 */
                if (latitude != 0 && longitude != 0) {
                    imageViewLocation.setImageResource(R.drawable.ic_location_found);
                    textViewLocation.setText("Localiza????o obtida!");
                } else {
                    imageViewLocation.setImageResource(R.drawable.ic_location_search);
                    textViewLocation.setText("Procurando localiza????o...");
                }

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

        // Verifica as permi????es.
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
        }
    }

    /**
     * TODO M??todo tempor??rio.
     */
    private void showUserLocation() {

        String message = "Latitude: " + latitude + "\n" +
                            "Longitude: " + longitude + "\n" +
                            "Endere??o: " + addressLine;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Localiza????o");
        builder.setMessage(message);
        builder.setNeutralButton("Enviar M??dia", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getActivity(), SendMediaActivity.class));
            }
        });
        builder.setPositiveButton("OK", null);
        builder.create().show();

    }

    private void cantShowUserLocation(String phone) {
        String message = "N??o foi poss??vel acessar o servi??o de localiza????o, deseja realizar uma chamada?";

        phoneNumberCall = phone;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Localiza????o");
        builder.setMessage(message);
        builder.setPositiveButton("N??o", null);
        builder.setNegativeButton("Chamar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Intent intent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phoneNumberCall, null));
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumberCall, null));
                startActivity(intent);
            }
        });
        builder.create().show();
    }

    /**
     * M??todo respons??vel por enviar a todos os contados de emerg??ncia do usu??rio, uma mensagem de socorro.
     */
    private void sendSMSMessage() {

        EmmergencyContactDAO ecDao = new EmmergencyContactDAO(getActivity());
        contacts = (ArrayList<EmmergencyContact>) ecDao.list();

        if (contacts.size() > 0) {
            String message = "Socorro! Preciso de Ajuda, estarei enviando minha localiza????o...";

            for (EmmergencyContact c : contacts) {
                String number = c.getNumber();

                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(number, null, message, null, null);
                    smsManager.sendTextMessage(number, null, addressLine, null, null);

                    Toast.makeText(getActivity(), "Mensagem enviada para: "  + number, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "N??o foi poss??vel avisar um ou mais de seus contatos", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        } else {
            Toast.makeText(getActivity(), "Voc?? n??o possui nenhum contato de emerg??ncia para ser avisado", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            locationManager.removeUpdates(locationListener);
            locationManager = null;
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
