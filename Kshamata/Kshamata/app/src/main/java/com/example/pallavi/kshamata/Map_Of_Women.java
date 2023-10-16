package com.example.pallavi.kshamata;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
/*
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
*/
//import android.location.LocationListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.Random;

public class Map_Of_Women extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    GoogleMap mMap;
    private static final int ERROR_DIALOG_REQUEST = 703;
    GoogleApiClient locationClient;
    LocationListener locListener;
    Marker from_marker_ref;
    MarkerOptions from_marker;
    LocationManager locationManager;
    Double[] lattarray = {12.9577,12.9787,13.6591,13.7700,13.3230,14.3991,13.9224,12.7231,12.18522,13.199};
    Double[] longarray = {77.6764,77.5285,78.0032,76.6257,77.6457,78.3452,77.1449,77.1415,78.37044,76.5932};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (servicesOK()) {
            setContentView(R.layout.activity_map__of__women);
            if (initMap()) {
                locationClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API)
                        .addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
                locationClient.connect();
            } else {
                Toast.makeText(getApplicationContext(), "Map not connected", Toast.LENGTH_SHORT).show();
            }
        } else {
            setContentView(R.layout.activity_map__of__women);

        }
        Location location = new Location("");
        for(int i=0;i<10;i++){
            location.setLatitude(lattarray[i]);
            location.setLongitude(longarray[i]);
            LatLng myLocation = new LatLng(location.getLatitude(),location.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(myLocation, 15);
            mMap.animateCamera(update);
            from_marker = new MarkerOptions().position(myLocation).icon(
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).alpha((float) 0.9);
            from_marker_ref = mMap.addMarker(from_marker);
        }
    }

    public boolean servicesOK() {
        int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable, this, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(getApplicationContext(), "Google play Services Not Available", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public boolean initMap() {
        if (mMap == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapofw);
            mMap = mapFragment.getMap();
        }
        return (mMap != null);
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
