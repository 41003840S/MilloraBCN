package com.example.manuel.millorabcn;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

public class MyApplication extends Application implements LocationListener {

    Location location;
    ProgressDialog progressDialog;
    private LocationManager locationManager;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //ProgressDialog que se muestra hasta que la aplicacion coge la localizacion
        /*progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Locating");
        progressDialog.show();*/

        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        // Intent intent = new Intent(this, MapActivity.class);
        //startActivity(intent);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        Toast.makeText(MyApplication.this, location.getLatitude() +", " + location.getLongitude() , Toast.LENGTH_SHORT).show();

        EventBus.getDefault().post(new OnLocationChangedEvent(location));
        //progressDialog.hide();
//        Intent intent = new Intent(this, MapFragment.class);
//        intent.putExtra("latitude", this.location.getLatitude());
//        intent.putExtra("longitude", this.location.getLongitude());
//        Log.e("LATITUDE_ENVIANDO", this.location.getLatitude() + " -------------------------");
//        Log.e("LONGITUDE_ENVIANDO", this.location.getLongitude() + " -------------------------");
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
}