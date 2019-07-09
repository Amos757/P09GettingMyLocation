package com.example.p09_gettingmylocation;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


public class MyService extends Service {

    boolean started;
    //LocationDetection
    FusedLocationProviderClient client = null;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onCreate() {
        Log.d("Service", "Service created");

        //LocationDetection settings
        client = LocationServices.getFusedLocationProviderClient(this);
        final LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setSmallestDisplacement(100);
        final LocationCallback mLocationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    Location data = locationResult.getLastLocation();
                    double lat = data.getLatitude();
                    double lng = data.getLongitude();
                    String msg = "New Loc Detected\nLat: " + lat + "  Lng: " + lng;
                    Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
                    
                }
            }
        };
        int permissionCheck = PermissionChecker.checkSelfPermission(MyService.this, Manifest.permission.ACCESS_FINE_LOCATION);

        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    String msg = "Lat: " + location.getLatitude() + " Lng: " + location.getLongitude();
                    Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
                } else {
                    String msg = "No Last Known Location Found";
                    Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
                }
            }
        });

        super.onCreate();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (started == false) {
            started = true;
            Log.d("Service", "Service started");
        } else {
            Log.d("Service", "Service is still running");
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d("Service", "Service exited");
        super.onDestroy();
    }


}
