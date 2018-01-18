package com.rkhs.c_andorid.facebookintegration.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.location.LocationListener;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rkhs.c_andorid.facebookintegration.R;

/**
 * Created by Admin on 16-01-2018.
 */

public class FragmentTwo extends Fragment implements OnMapReadyCallback {


    GoogleMap kGoogleMap;
    MapView kMapView;
    View view;

    boolean updatedOnce = false;

    LocationListener locationListener;
    LocationManager locationManager;
    LatLng kLocation;

    public static final FragmentTwo newInstance(String paramString) {
        FragmentTwo fragmentTwo = new FragmentTwo();
        Bundle bundle = new Bundle();
        bundle.putString("EXTRA_MESSAGE", paramString);
        fragmentTwo.setArguments(bundle);
        return fragmentTwo;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_two, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initMap();

    }

    private void initMap() {

//        lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//        ll = new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//
//                kLocation = location;
//                //refreshMap();
//            }
//        };

        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        kGoogleMap = googleMap;
        if (kGoogleMap != null) {
            // enable map zoom
            kGoogleMap.getUiSettings().setZoomControlsEnabled(true);

            // disable map interaction
            kGoogleMap.getUiSettings().setScrollGesturesEnabled(true);
            kGoogleMap.getUiSettings().setZoomGesturesEnabled(false);
            kGoogleMap.getUiSettings().setRotateGesturesEnabled(false);
            kGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
            kGoogleMap.getUiSettings().setTiltGesturesEnabled(false);

            //onclick it direct us to the maps application or to google maps...
            kGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng arg0) {
                    //openMaps(false);
                }
            });

            //7_12
            //setting current Location on the map
            setCurrentLocationOnMap();
        }

    }

    //7_12
    private void setCurrentLocationOnMap() {

        locationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        locationListener = new android.location.LocationListener() {

            @Override
            public void onLocationChanged(Location location) {

                //rohans code...
                //refreshing the current map position...
                //LatLng defaultPosition = new LatLng(37.78116, -122.39422);

                if (!updatedOnce) {
                    LatLng defaultPosition = new LatLng(location.getLatitude(), location.getLongitude());
                    moveToLocation(defaultPosition);
                    updatedOnce = true;
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, locationListener);
    }

    private void moveToLocation(LatLng coordinates) {
        kLocation = coordinates;
        if (kGoogleMap == null) {
            return;
        }

        float zoom = kGoogleMap.getCameraPosition().zoom;
        if (zoom < 5) {
            zoom = 17.0f;
        }
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(coordinates,
                zoom);
        kGoogleMap.moveCamera(update);

    }
}
