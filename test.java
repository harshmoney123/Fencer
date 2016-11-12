package com.arunabh.here;

import android.content.IntentSender;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Service;
import android.location.*;
import android.content.Context;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.plus.Plus;

public class test extends AppCompatActivity {
    GoogleApiClient client = new GoogleApiClient.Builder(this)
            .enableAutoManage(this, (OnConnectionFailedListener)this)
            .addApi(Plus.API)
            .addScope(Plus.SCOPE_PLUS_LOGIN)
            .setAccountName("arunabhsingh15@gmail.com")
            .build();

    protected Location lastLoc;
    protected TextView latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        if (client == null) {
            client = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks((ConnectionCallbacks)this)
                    .addOnConnectionFailedListener((OnConnectionFailedListener) this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    protected void onStart() {
        client.connect();
        super.onStart();
    }

    protected void onStop() {
        client.disconnect();
        super.onStop();
    }

    public void getLocation() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    }

    public void onConnected(Bundle connectionHint) {
        try {
            lastLoc = LocationServices.FusedLocationApi.getLastLocation(client);
        }
        catch(SecurityException e) {
            return;
        }

        if (lastLoc != null) {
            latitude.setText(String.valueOf(lastLoc.getLatitude()));
            longitude.setText(String.valueOf(lastLoc.getLongitude()));
        }

        //if (mRequestingLocationUpdates) {
            startLocationUpdates();
        //}
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                client, createLocationRequest(), LocationListener.onLocationChanged(lastLoc));
    }

    protected LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(client,
                        builder.build());

        return mLocationRequest;
    }

    public void onResult(LocationSettingsResult result) {
        final Status status = result.getStatus();
        final LocationSettingsStates = result.getLocationSettingsStates();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                // All location settings are satisfied. The client can
                // initialize location requests here.
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                /*// Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    status.startResolutionForResult(
                            OuterClass.this,
                            REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    // Ignore the error.
                }*/
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                break;
        }
    }
}

