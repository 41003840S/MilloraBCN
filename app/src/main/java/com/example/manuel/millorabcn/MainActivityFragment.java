package com.example.manuel.millorabcn;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    ListView listReport;
    TextView reportTitle, reportDescription;
    ImageView imageView;
    VideoView videoView;
    int position1 = 0;
    Intent intent;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainFragment = inflater.inflate(R.layout.fragment_main, container, false);

        listReport = (ListView) mainFragment.findViewById(R.id.reportList);

        //Le decimos a Firebase que este sera el contexto
        Firebase.setAndroidContext(getContext());

        //Creamos una referencia a nuestra bd de Firebase
        Firebase refNotes = new Firebase("https://millorabcn.firebaseio.com/").child("reports");

        final FirebaseListAdapter adapter = new FirebaseListAdapter<Report>(getActivity(), Report.class, R.layout.report_row, refNotes) {
            @Override
            protected void populateView(View v, Report model, int position) {
                super.populateView(v, model, position);

                position1 = position;
                reportTitle = (TextView) v.findViewById(R.id.textTitle);
                reportDescription = (TextView) v.findViewById(R.id.textReport);
                imageView = (ImageView) v.findViewById(R.id.photoReport);
                videoView = (VideoView) v.findViewById(R.id.videoReport);

                imageView.setVisibility(View.INVISIBLE);
                videoView.setVisibility(View.INVISIBLE);

                reportTitle.setText(model.getTitle());
                reportDescription.setText(model.getNota());

                if(model.getImagePath()!= null) {
                    imageView.setVisibility(View.VISIBLE);
                    File imagePath = new File(model.getImagePath());
                    Picasso.with(getContext()).load(imagePath).fit().into(imageView);
                }
                if(model.getVideoPath()!= null) {
                    MediaController mediaController = new MediaController(getContext());
                    videoView.setVisibility(View.VISIBLE);
                    videoView.setVideoPath(model.getVideoPath());
                    videoView.setMediaController(mediaController);
                    mediaController.hide();
                    //videoView.requestFocus();
                    videoView.start();

                }
                if(model.getImagePath() == null && model.getVideoPath()== null){
                    imageView.setVisibility(View.VISIBLE);
                    Picasso.with(getContext()).load(R.drawable.noimageavailable).fit().into(imageView);
                }
            }
        };

        listReport.setAdapter(adapter);


        return mainFragment;
    }
}
