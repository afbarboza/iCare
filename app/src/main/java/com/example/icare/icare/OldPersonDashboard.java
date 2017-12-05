/*
 * Copyright (c) 2017.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.example.icare.icare;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OldPersonDashboard extends AppCompatActivity
        implements  GoogleApiClient.OnConnectionFailedListener,
                    GoogleApiClient.ConnectionCallbacks
{
    /* request code to use GPS */
    private static final int MY_PERMISSION_REQUEST_LOCATION = 99;

    /* API Client to use GPS */
    GoogleApiClient mGoogleApiClient;

    private FirebaseDatabase database;
    private DatabaseReference ref;
    private static boolean calledAlready = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_person_dashboard);

        FirebaseApp.initializeApp(OldPersonDashboard.this);
        Log.i("alex", "Firebase initialized.");

        if (!calledAlready){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            calledAlready = true;
        }
        Log.i("alex", "Local persistence enabled");


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        ref.child("drougs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Droug> listOfDrougs = new ArrayList<Droug>();
                listOfDrougs.clear();
                for(DataSnapshot objectSnapshot: dataSnapshot.getChildren()){
                    Droug drougReceived = objectSnapshot.getValue(Droug.class);
                    listOfDrougs.add(drougReceived);
                    Log.e("testeGiovanni", drougReceived.getName());
                }

                if(!listOfDrougs.isEmpty() && listOfDrougs != null)
                createListView(listOfDrougs);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /* initializes the google API client to use GPS */
        if (checkLocationPermission()) {
            if (mGoogleApiClient == null) {
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
            }
        }

        /* sets an test alarm */
        DrugAlarm d = new DrugAlarm();
        d.addAlarm(this, 1, 1);
    }

    public void createListView(List<Droug> drougList){
        ListView lv_OldPerson = (ListView) findViewById(R.id.lv_old_person_drougs);
        final ListDrougsAdapter listDrougsAdapter = new ListDrougsAdapter(OldPersonDashboard.this, drougList);
        lv_OldPerson.setAdapter(listDrougsAdapter);
    }

    /**
     * getCurrentAddress - converts the latitude and longitude into
     *                      a readable address.
     *
     * @param latitude: the latitude of the old person
     * @param longitude: the longitude of the old person
     *
     * @return the readable user address
     */
    public String getCurrentAddress(double latitude, double longitude) {
        /* readable user address string */
        String currAddress = "";

        /* A class for handling geocoding and reverse geocoding */
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        /* List of captured addresses */
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            currAddress = getString(R.string.old_invalid_addr);
            return currAddress;
        }

        /* handles the case the current location does not has a valid address */
        if (addresses == null || addresses.size() == 0) {
            currAddress = getString(R.string.old_invalid_addr);
            return currAddress;
        }

        Address addr = addresses.get(0);
        for (int i = 0; i <= addr.getMaxAddressLineIndex(); i++) {
            currAddress += addr.getAddressLine(i);
        }

        return currAddress;
    }

    /**
     * gets the caregiver email address.
     * @return
     */
    public String retrieveCaregiverEmail() {
        return "alex.barboza@usp.br";
    }

    /**
     * sendCaregiverLocationsInfo - sends to the caregiver informations about the old person current
     *                              locations. This allows caregiver keeps a track of the pacient
     *                              last locations.
     *
     * @param coordinates: the string format storing latitude and longitude. Not null.
     * @param address: the street, house number, zip code and city information. NOt null.
     */
    public void sendCaregiverLocationsInfo(String coordinates, String address) {
        /* defines mail body text */
        String mailBody = getString(R.string.old_email_body);
        mailBody += coordinates;
        mailBody += "\n\n\n>>>" + address;

        /* defines the intent which will be used to send email */
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{retrieveCaregiverEmail()});
        i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.old_email_title));
        i.putExtra(Intent.EXTRA_TEXT, mailBody);

        /* sends email */
        startActivity(Intent.createChooser(i, getString(R.string.old_mail_sending)));
    }

    /**
     * Callback for the result from requesting permissions.
     *
     * @param requestCode The request code passed
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     */
    public void onRequestPermissionsResult (int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PersonalToast.toastMessage(this, getString(R.string.old_dashboard_gpsok));
                }
        }
    }

    /**
     * @see GoogleApiClient
     * @param bundle
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        final AppCompatActivity instance = this;

        if (!checkLocationPermission()) {
            return;
        }

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            /* needed informations to get current address */
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();

            /* sends mail to the proper caregiver*/
            String coordinates = "Latitude: " + latitude  + "\nLongitude: " + longitude;
            String address = getCurrentAddress(latitude, longitude);
            sendCaregiverLocationsInfo(coordinates, address);
        }
    }

    /**
     * @see GoogleApiClient
     * @param i
     */
    @Override
    public void onConnectionSuspended(int i) {

    }

    /**
     * @see GoogleApiClient
     */
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    /**
     * @see GoogleApiClient
     */
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    /**
     * @see GoogleApiClient
     * @param connectionResult
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * Checks whether the app acquired the permission to use GPS
     *
     * @return  true, if app has the permission
     *          false, otherwise.
     *
     */
    public boolean checkLocationPermission() {
        int currPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (currPermission != PackageManager.PERMISSION_GRANTED) {
            /* warns user about the need of enabling gps */
            String errTitle = getString(R.string.old_dashboard_error);
            String errText = getString(R.string.old_dashboard_gpserror);
            new AlertDialog.Builder(this).setTitle(errTitle).setMessage(errText).show();

            /* Request permission to use GPS during runtime execution */
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_REQUEST_LOCATION);

            return false;
        } else {
            return true;
        }
    }
}
