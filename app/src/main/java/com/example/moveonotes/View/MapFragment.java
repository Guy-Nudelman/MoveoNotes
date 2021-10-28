package com.example.moveonotes.View;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import com.example.moveonotes.Model.Note;
import com.example.moveonotes.R;
import com.example.moveonotes.Utils.GlobalApplicationContext;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapFragment extends DialogFragment implements OnMapReadyCallback {

    //Variables
    private MapView mapView;
    private GoogleMap map;
    private List<Note> noteList;
    private Map<Marker, Note> allMarkersMap;
    private List<Address> addresses;
    private Geocoder geocoder;
    public static final double DEFAULT_LAT = 31.894756;
    public static final double DEFAULT_LONG = 34.809322;
    public static final int DEFAULT_ZOOM = 5;
    private String address;


    //Constructors
    public MapFragment(List<Note> notes) {
        this.noteList = notes;
    }

    public MapFragment() {

    }

    //New Instances Require To Update Map
    public static MapFragment newInstance(List<Note> notes) {
        MapFragment mapFragment = new MapFragment();
        mapFragment.geocoder = new Geocoder(GlobalApplicationContext.getContext(), Locale.getDefault());
        if (notes != null) mapFragment.noteList = notes;
        return mapFragment;
    }


    //Interface Methods
    @Override
    public Dialog onCreateDialog(Bundle SaveInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_map, null);
        builder.setView(view);
        mapView = (MapView) view.findViewById(R.id.map_map_view);
        mapView.onCreate(SaveInstanceState);
        mapView.getMapAsync(this);
        return builder.create();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(true);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //  ActivityCompat#requestPermissions
            return;
        }
        map.setMyLocationEnabled(true);
        //in old Api Needs to call MapsInitializer before doing any CameraUpdateFactory call
        try {
            MapsInitializer.initialize(this.getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Updates the location and zoom of the MapView

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(43.1, -87.9), 10);
        map.animateCamera(cameraUpdate);
        //UseFullFunctions
        LatLng latLng = new LatLng(DEFAULT_LAT, DEFAULT_LONG);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
        try {
            addNotesMarkers();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                Note note = allMarkersMap.get(marker);
                goToEditPage(note);
                return false;
            }
        });


    }

    private void goToEditPage(Note note) {
        Intent intent = new Intent(getActivity(), AddEditNoteActivity.class);
        intent.putExtra(AddEditNoteActivity.EXTRA_ID, note.getKey());
        intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, note.getNoteTitle());
        intent.putExtra(AddEditNoteActivity.EXTRA_BODY, note.getNoteBody());
        intent.putExtra(AddEditNoteActivity.EXTRA_ID, note.getKey());
        intent.putExtra(AddEditNoteActivity.EXTRA_IMAGE_URL, note.getImage());
        startActivityForResult(intent, 2);

    }

    private void addNotesMarkers() throws IOException {

        allMarkersMap = new HashMap<Marker, Note>();
        for (Note note : noteList
        ) {
            addresses = geocoder.getFromLocation(note.getNoteLat(), note.getNoteLong(), 1);
            if (addresses.size()>0) {
                address = addresses.get(0).getAddressLine(0);
                if (address == "") address = "No Address";
            }
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(new LatLng(note.getNoteLat(), note.getNoteLong()))
                    .title(note.getNoteTitle())
                    .snippet(address));
            marker.showInfoWindow();
            allMarkersMap.put(marker, note);
        }


    }


    //LifeCycle Methods
    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
