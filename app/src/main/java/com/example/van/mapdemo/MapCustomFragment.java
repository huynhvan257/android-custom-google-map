package com.example.van.mapdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * Created by van on 10/21/15.
 */
public class MapCustomFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private UiSettings uiSettings;
    private OnMapReadyCallback onMapReadyCallback;
    private boolean loaded;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.map_fragment, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        uiSettings = mMap.getUiSettings();

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                loaded = true;
            }
        });

        setZoomControlsEnabled(true);
        setMyLocationEnabled(true);

        if (onMapReadyCallback != null) {
            onMapReadyCallback.onMapReady(mMap);
        }
    }

    /**
     * This method is used set OnMapReadyCallback.
     *
     * @param onMapReadyCallback
     */
    public void setOnMapReadyCallback(OnMapReadyCallback onMapReadyCallback) {
        this.onMapReadyCallback = onMapReadyCallback;
    }

    /**
     * This method is used get current Google Map.
     *
     * @return
     */
    public GoogleMap getGoogleMap() {
        return mMap;
    }

    /**
     * This method is used set zoom controls enabled.
     *
     * @param enabled
     */
    public void setZoomControlsEnabled(boolean enabled) {
        uiSettings.setZoomControlsEnabled(enabled);
        mMap.setTrafficEnabled(true);
    }

    /**
     * This method is used set my location enabled.
     *
     * @param enabled
     */
    public void setMyLocationEnabled(boolean enabled) {
        mMap.setMyLocationEnabled(enabled);
    }

    /**
     * This method is used draw list marker and fit zoom all marker.
     *
     * @param latLngList
     */
    public void drawListMarker(List<InfoMarker> latLngList) {
        if (latLngList == null) {
            return;
        }

        mMap.clear();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (InfoMarker latLng : latLngList) {
            mMap.addMarker(new MarkerOptions().position(latLng.getLatLng()).title(latLng.getTitle()).icon(latLng.getIcon()));
            builder.include(latLng.getLatLng());
        }
        CameraUpdate cameraUpdate;
        if (loaded) {
            cameraUpdate = CameraUpdateFactory
                    .newLatLngBounds(builder.build(), 100);
        } else {
            cameraUpdate = CameraUpdateFactory.newLatLng(builder.build().getCenter());
        }
        mMap.animateCamera(cameraUpdate);
    }
}
