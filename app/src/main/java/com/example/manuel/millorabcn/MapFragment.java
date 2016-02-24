package com.example.manuel.millorabcn;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.views.MapView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class MapFragment extends Fragment {

    private MapView mapView = null;
    private Location location = null;
    double latitude;
    double longitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onLocationChangedEvent(OnLocationChangedEvent event){
        //Recogemos el intent con la localizacion que nos da MyAplication
        latitude = event.getLocation().getLatitude();
        longitude = event.getLocation().getLongitude();

        Log.e("LATITUDE_RECIBIEDO", latitude + " -------------------------");
        Log.e("LONGITUDE_RECIBIEDO", longitude + " -------------------------");

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude,longitude))     // Sets the center of the map to Barcelona  41.3851, 2.1734
                .zoom(11)                                  // Sets the zoom
                .build();

        mapView.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mapFragment =  inflater.inflate(R.layout.fragment_map, container, false);

        /** Create a mapView and give it some properties */
        mapView = (MapView) mapFragment.findViewById(R.id.mapview);

        mapView.onCreate(savedInstanceState);

        //mapView.setStyleUrl(Style.MAPBOX_STREETS);
        mapView.setStyleUrl("mapbox://styles/nehalem/cil015xln005lc9m3f8zekb4i");
        mapView.setRotateEnabled(false);
        mapView.setZoomControlsEnabled(true);



        return mapFragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause()  {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
