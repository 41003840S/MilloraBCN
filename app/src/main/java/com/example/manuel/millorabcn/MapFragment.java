package com.example.manuel.millorabcn;

import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.MyLocationTracking;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngZoom;
import com.mapbox.mapboxsdk.views.MapView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class MapFragment extends Fragment {

    public MapView mapView = null;
    private Location location = null;
    double latitude;
    double longitude;

    public MapFragment() {
        // Required empty public constructor
    }

    //Nos subscribimos al Evento que captura la Localizacion
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    //Recogemos el evento con la localizacion que nos da el LocationChangedEvent al que estamos suscritos
    @Subscribe
    public void onLocationChangedEvent(LocationChangedEvent event) {

        latitude = event.getLocation().getLatitude();
        longitude = event.getLocation().getLongitude();
        //Para verificar la posicion
        Log.e("LATITUDE_RECIBIEDO", latitude + " -------------------------");
        Log.e("LONGITUDE_RECIBIEDO", longitude + " -------------------------");

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))     // Sets the center of the map to Barcelona  41.3851, 2.1734
                .zoom(15)                                  // Sets the zoom
                .build();

        mapView.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mapFragment =  inflater.inflate(R.layout.fragment_map, container, false);

        /** Create a mapView and give it some properties */
        mapView = (MapView) mapFragment.findViewById(R.id.mapview);

        mapView.onCreate(savedInstanceState);

        setMap();

        //BOTON para ir a la posicion actual
        FloatingActionButton myLocationButton = (FloatingActionButton) mapFragment.findViewById(R.id.myLocationButton);
        myLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mapView.getMyLocation() != null) { // Check to ensure coordinates aren't null, probably a better way of doing this...
                    mapView.setCenterCoordinate(new LatLngZoom(mapView.getMyLocation().getLatitude(), mapView.getMyLocation().getLongitude(), 20), true);
                }
            }
        });

        //Le decimos a Firebase que este sera el contexto
        Firebase.setAndroidContext(getContext());

        //Creamos una referencia a nuestra bd de Firebase y a su hijo
        final Firebase notes = new Firebase("https://millorabcn.firebaseio.com/").child("reports");

        notes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //Recorremos todas las notas que haya en ese momento
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    //Creamos un objeto nota de ese elemento
                    Report report = postSnapshot.getValue(Report.class);
                    mapView.addMarker(new MarkerOptions()
                            .position(new LatLng(report.getLatitud(), report.getLongitud()))
                            .title(report.getTitle())
                            .snippet("HOLAAAAAAAAAAAAA"));


                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }

        });


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

    public void setMap(){
        //mapView.setStyleUrl(Style.MAPBOX_STREETS);
        mapView.setStyleUrl("mapbox://styles/nehalem/cil015xln005lc9m3f8zekb4i");
        mapView.setRotateEnabled(false);
        mapView.setMyLocationEnabled(true);
        //mapView.setZoomControlsEnabled(true);
        //mapView.setMyLocationTrackingMode(MyLocationTracking.TRACKING_FOLLOW);
    }
}
