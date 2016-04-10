package com.example.manuel.millorabcn;

import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.firebase.client.Firebase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddActivityFragment extends Fragment{

    EditText addTitle, addNote;
    ImageButton cameraButton, galleryButton, micButton;
    ImageView checkGallery, checkCamera, checkMic;
    boolean tookPhoto = false;
    boolean tookVideo = false;
    boolean tookAudio = false;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    double latitude = 0;
    double longitude = 0;

    public AddActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //Nos subscribimos al Evento que captura la Localizacion
        EventBus.getDefault().register(this);
    }

    //Recogemos el evento con la localizacion que nos da el LocationChangedEvent al que estamos suscritos
    @Subscribe
    public void onLocationChangedEvent(LocationChangedEvent event) {

        latitude = event.getLocation().getLatitude();
        longitude = event.getLocation().getLongitude();

        //Para verificar la posicion
        Log.e("LATITUDE_RECIBIEDO_ADD", latitude + " -------------------------");
        Log.e("LONGITUDE_RECIBIEDO_ADD", longitude + " -------------------------");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_add, container, false);

        addTitle = (EditText) view.findViewById(R.id.addTitle);
        addNote = (EditText) view.findViewById(R.id.addNote);
        checkGallery = (ImageView) view.findViewById(R.id.checkGallery);
        checkCamera = (ImageView) view.findViewById(R.id.checkCamera);
        checkMic = (ImageView) view.findViewById(R.id.checkMic);
        checkGallery.setVisibility(View.INVISIBLE);
        checkCamera.setVisibility(View.INVISIBLE);
        checkMic.setVisibility(View.INVISIBLE);

        //Imagebuttons. Pero tambien se podria hacer con el layout qque coge el imagebutton y el texto
        galleryButton = (ImageButton) view.findViewById(R.id.galleryButton);
        cameraButton = (ImageButton) view.findViewById(R.id.cameraButton);
        micButton = (ImageButton) view.findViewById(R.id.micButton);

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openVideo();
                checkGallery.setVisibility(View.VISIBLE);
                //Toast.makeText(getContext(), "Attached picture", Toast.LENGTH_SHORT).show();

            }
        });
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
                checkCamera.setVisibility(View.VISIBLE);
                //Toast.makeText(getContext(), "Attached photo", Toast.LENGTH_SHORT).show();
            }
        });
        micButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMic();
                checkMic.setVisibility(View.VISIBLE);
                //Toast.makeText(getContext(), "Attached voice note", Toast.LENGTH_SHORT).show();
            }
        });

        //Le decimos a Firebase que este sera el contexto
        Firebase.setAndroidContext(getContext());

        //Creamos una referencia a nuestra bd de Firebase
        Firebase ref = new Firebase("https://millorabcn.firebaseio.com/");

        //Y tendra un hijo que es donde guardara las notas y la localizacion de esta
        final Firebase notes = ref.child("reports");

        FloatingActionButton sendNote = (FloatingActionButton) view.findViewById(R.id.sendNote);
        sendNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Firebase note = notes.push();

                Report nota = new Report();
                nota.setTitle(addTitle.getText().toString());
                nota.setNota(addNote.getText().toString());
                nota.setLatitud(latitude);
                nota.setLongitud(longitude);
                if (tookPhoto) {
                    nota.setImagePath(imageFile());
                    //Lo ponemos a false otra vez
                    tookPhoto = false;
                }
                if (tookVideo) {
                    nota.setVideoPath(videoFile());
                    //Lo ponemos a false otra vez
                    tookVideo = false;
                }
                //Instruccion para subir a Firebase
                note.setValue(nota);

                //Vaciamos el editTextNota
                addTitle.setText("");
                addNote.setText("");

                //Escondemos los iconos
                checkGallery.setVisibility(View.INVISIBLE);
                checkCamera.setVisibility(View.INVISIBLE);
                checkMic.setVisibility(View.INVISIBLE);
                //Toast.makeText(getContext(), videoFile(), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    public void openVideo(){
        tookVideo = true;
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takeVideoIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void openCamera(){
        tookPhoto = true;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void openMic(){
        final int ACTIVITY_RECORD_SOUND = 1;
        Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        startActivityForResult(intent, ACTIVITY_RECORD_SOUND);
    }

    public String videoFile() {

        //Cogemos el PATH del ultimo video tomado
        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = getActivity().managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
        cursor.moveToLast();
        String path =  cursor.getString(column_index_data);

        return path;
    }

    public String imageFile() {

        //Cogemos el PATH de la ultima foto tomada
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToLast();
        String path =  cursor.getString(column_index_data);

        return path;
    }

}
