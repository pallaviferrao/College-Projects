package com.example.pallavi.travell;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;


import android.app.Activity;

import android.app.ProgressDialog;

import android.content.Context;

import android.location.Location;

import android.location.LocationListener;

import android.location.LocationManager;

import android.os.AsyncTask;

import android.os.Bundle;

import android.view.View;

import android.view.View.OnClickListener;

import android.widget.Button;

import android.widget.Toast;

public class LbsGeocodingActivity extends Activity {

    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters

    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds

    private GeoCoder geoCoder = new GeoCoder();

    protected LocationManager locationManager;

    protected Location currentLocation;

    protected Button retrieveLocationButton;

    protected Button reverseGeocodingButton;

    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        retrieveLocationButton = (Button) findViewById(R.id.retrieve_location_button);

        reverseGeocodingButton = (Button) findViewById(R.id.reverse_geocoding_button);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(

                LocationManager.GPS_PROVIDER,

                MINIMUM_TIME_BETWEEN_UPDATES,

                MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,

                new MyLocationListener()
        );

        retrieveLocationButton.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                showCurrentLocation();
            }
        });


        reverseGeocodingButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                performReverseGeocodingInBackground();
            }
        });


    }


    protected void performReverseGeocodingInBackground() {
        showCurrentLocation();

        new ReverseGeocodeLookupTask().execute((Void[]) null);

    }

    protected void showCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (currentLocation != null) {

            String message = String.format(

                    "Current Location \n Longitude: %1$s \n Latitude: %2$s",

                    currentLocation.getLongitude(), currentLocation.getLatitude()

            );

            Toast.makeText(LbsGeocodingActivity.this, message,

                    Toast.LENGTH_LONG).show();

        }



    }



    private class MyLocationListener implements LocationListener {



        public void onLocationChanged(Location location) {

            String message = String.format(

                    "New Location \n Longitude: %1$s \n Latitude: %2$s",

                    location.getLongitude(), location.getLatitude()

            );
            Toast.makeText(LbsGeocodingActivity.this, message, Toast.LENGTH_LONG).show();

        }



        public void onStatusChanged(String s, int i, Bundle b) {

            Toast.makeText(LbsGeocodingActivity.this, "Provider status changed",

                    Toast.LENGTH_LONG).show();

        }



        public void onProviderDisabled(String s) {

            Toast.makeText(LbsGeocodingActivity.this,

                    "Provider disabled by the user. GPS turned off",

                    Toast.LENGTH_LONG).show();

        }



        public void onProviderEnabled(String s) {

            Toast.makeText(LbsGeocodingActivity.this,

                    "Provider enabled by the user. GPS turned on",

                    Toast.LENGTH_LONG).show();

        }



    }


    public class ReverseGeocodeLookupTask extends AsyncTask<Void, Void, GeoCodeResult> {



        private ProgressDialog progressDialog;



        @Override

        protected void onPreExecute() {

            this.progressDialog = ProgressDialog.show(

                    LbsGeocodingActivity.this,

                    "Please wait...contacting Yahoo!", // title

                    "Requesting reverse geocode lookup", // message

                    true // indeterminate

            );

        }


        @Override

        protected GeoCodeResult doInBackground(Void... params) {

            return geoCoder.reverseGeoCode(currentLocation.getLatitude(), currentLocation.getLongitude());

        }



        @Override

        protected void onPostExecute(GeoCodeResult result) {

            this.progressDialog.cancel();

            Toast.makeText(LbsGeocodingActivity.this, result.toString(), Toast.LENGTH_LONG).show();

        }



    }



}
