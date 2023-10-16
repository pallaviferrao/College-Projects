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


public class track_woman extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
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
    Random rand;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (servicesOK()) {
            setContentView(R.layout.activity_track_woman);
            if (initMap()) {
                locationClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API)
                        .addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
                locationClient.connect();
            } else {
                Toast.makeText(getApplicationContext(), "Map not connected", Toast.LENGTH_SHORT).show();
            }
        } else {
            setContentView(R.layout.activity_track_woman);

        }
        rand = new Random();
        int i = rand.nextInt(9) + 0;
        Location location = new Location("");
        location.setLatitude(lattarray[i]);
        location.setLongitude(longarray[i]);
        LatLng myLocation = new LatLng(location.getLatitude(),location.getLongitude());
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(myLocation, 15);
        mMap.animateCamera(update);
        from_marker = new MarkerOptions().position(myLocation).icon(
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).alpha((float) 0.9);
        from_marker_ref = mMap.addMarker(from_marker);

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
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mMap = mapFragment.getMap();
        }
        return (mMap != null);
    }

    public void cur_loc_give(View view) {
        Log.d("log1", "Entered method");
        /*Location location = new Location("");
        location.setLatitude(12.9716);
        location.setLongitude(77.5946);
        LatLng myLocation = new LatLng(location.getLatitude(),location.getLongitude());
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(myLocation, 15);
        mMap.animateCamera(update);
        from_marker = new MarkerOptions().position(myLocation).icon(
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)).alpha((float) 0.1);
        from_marker_ref = mMap.addMarker(from_marker);
        mMap.setMyLocationEnabled(true);*/
        locListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("log1", "Entered method location change");
                LatLng myLocation = new LatLng(location.getLatitude(),location.getLongitude());
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(myLocation, 15);
                mMap.animateCamera(update);
                from_marker = new MarkerOptions().position(myLocation).icon(
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                from_marker_ref = mMap.addMarker(from_marker);
            }

        };
        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(),"Toost",Toast.LENGTH_SHORT).show();
            // TODO: Consider calling
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
                        ,10);
            }
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }else {
            locationManager.requestLocationUpdates("gps", 5000, 0, (android.location.LocationListener) locListener);
        }*/
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
