package com.avichai98.farmroad.Manager;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.avichai98.farmroad.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class RecordsFragmentMap extends Fragment {
    SupportMapFragment mapFragment;
    private GoogleMap map;

    public RecordsFragmentMap(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_FRG_map);
        assert mapFragment != null;
        mapFragment.getMapAsync(googleMap -> map = googleMap);
        return view;
    }

    public void zoomOnLocation(double lat, double lon){
        if(map == null) {
            Log.d("MapFragment:", "googleMap is null");
            return;
        }
        LatLng location = new LatLng(lat,lon);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, 18f);
        map.animateCamera(cameraUpdate);
    }
}
