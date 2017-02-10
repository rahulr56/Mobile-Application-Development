package com.falc0n.inclass12;

import android.*;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationListener locationListener;
    LocationManager locationManager;
    PolylineOptions polylineOptions;
    boolean trackingFlag = false;
    private static final int REQUEST_LOCATION = 2;

    public final int MIN_DISTANCE = 10;
    public final int MIN_INTERVAL = 1000;
    ArrayList<LatLng> latLngArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        latLngArrayList = new ArrayList<>();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        polylineOptions = new PolylineOptions();
        polylineOptions.width(5).color(Color.BLUE).geodesic(true);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.getUiSettings().setCompassEnabled(true);

        //googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        //mMap.addMarker(new MarkerOptions().position(new LatLng(0,0).t))
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }
            return;
        } else {

        }

        mMap.setMyLocationEnabled(true);

        // Add a marker in Sydney and move the camera


        Location  location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location!=null)
        {
            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));

        }
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if(!trackingFlag)
                {
                    Toast.makeText(MapsActivity.this,"Start Location Tracking",Toast.LENGTH_SHORT).show();
                    trackingFlag = true;
                    latLngArrayList.clear();
                }else {
                    Toast.makeText(MapsActivity.this,"Stop Location Tracking",Toast.LENGTH_SHORT).show();
                    if(latLngArrayList.size()>0) {
                        mMap.addMarker(new MarkerOptions().position(latLngArrayList.get(0))).setTitle("Starting Position");

                        LatLngBounds.Builder builder = LatLngBounds.builder();
                   /* builder.include(latLngArrayList.get(0));
                    builder.include(latLngArrayList.get(latLngArrayList.size()-1));
                   */
                        for (LatLng latLng1 : latLngArrayList)
                        {
                            builder.include(latLng1);
                        }
                        LatLngBounds latLngBounds = builder.build();


                        mMap.addMarker(new MarkerOptions().position(latLngArrayList.get(latLngArrayList.size() - 1))).setTitle("Current Position");

  /*                      LatLngBounds latLngBounds;
                        try {
                            latLngBounds = new LatLngBounds(latLngArrayList.get(0), latLngArrayList.get(latLngArrayList.size() - 1));
                        }catch (IllegalArgumentException e)
                        {
                            latLngBounds = new LatLngBounds(latLngArrayList.get(latLngArrayList.size() - 1),latLngArrayList.get(0));
                        }
  */                      //mMap.setLatLngBoundsForCameraTarget(latLngBounds);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 11));
                    }
                    trackingFlag = false;
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("App", "In on resume");
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d("App", "loc not enabled");
            AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
            builder.setCancelable(false).setTitle("GPS not enabled")
                    .setMessage("Would you like to enable GPS settings?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Log.d("App", "loc changed!" + location.getLatitude() + "  " + location.getLongitude());
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    if(trackingFlag)
                    {
                        latLngArrayList.add(latLng);
                        updateMap();
                    }
                }


                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                }
                return;
            } else {

            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_INTERVAL, MIN_DISTANCE, locationListener);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // We can now safely use the API we requested access to
/*                Location myLocation =
                        LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);*/


            } else {
                // Permission was denied or request was cancelled
                finish();
            }
        }
    }

    private void updateMap() {
        if (mMap != null) {
            //mMap=((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMapAsync();
            polylineOptions = new PolylineOptions();
            for(LatLng latLng : latLngArrayList) {
                polylineOptions.add(latLng);
                Log.d("App", "In update map" + latLng.toString());
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngArrayList.get(latLngArrayList.size()-1), 16));
            polylineOptions.width(5).color(Color.BLUE).geodesic(true);
            mMap.addPolyline(polylineOptions);
        }

    }
}
