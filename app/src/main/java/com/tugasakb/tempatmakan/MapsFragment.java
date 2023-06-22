package com.tugasakb.tempatmakan;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
//Muhamad Dimas Azka Syarif Umair
//IF-4
//10120165
public class MapsFragment extends Fragment {

    // variabel
    FusedLocationProviderClient client;
    private GoogleMap peta;
    ArrayList<LatLng> arrayList = new ArrayList<LatLng>();
    LatLng bebek = new LatLng(-6.925706645612392, 107.5882034981669);
    LatLng mixue = new LatLng(-6.925609348901309, 107.58926806318792);
    LatLng seafood = new LatLng(-6.927274855170392, 107.58449032512792);
    LatLng baso = new LatLng(-6.922296911597685, 107.58610713102499);
    LatLng bolu = new LatLng(-6.922146244773465, 107.58865727585436);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inisialisasi view
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        // inisialisasi map Fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);

        // insialisai lokasi client
        client = LocationServices.getFusedLocationProviderClient(getActivity());
        arrayList.add(bebek);
        arrayList.add(mixue);
        arrayList.add(seafood);
        arrayList.add(baso);
        arrayList.add(bolu);

        //memanggil map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {

                //menambahkan marker pada map
                peta = googleMap;
                peta.addMarker(new MarkerOptions().position(bebek).title(getString(R.string.Tempat1)));
                peta.addMarker(new MarkerOptions().position(mixue).title(getString(R.string.Tempat2)));
                peta.addMarker(new MarkerOptions().position(seafood).title(getString(R.string.Tempat3)));
                peta.addMarker(new MarkerOptions().position(baso).title(getString(R.string.Tempat4)));
                peta.addMarker(new MarkerOptions().position(bolu).title(getString(R.string.Tempat5)));
                for (int i=0;i<arrayList.size();i++){
                    peta.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
                    peta.moveCamera(CameraUpdateFactory.newLatLng(arrayList.get(i)));
                }
            }
        });

        // melihat condition
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //jika lokasi diizinkan
            // memanggil method
            getCurrentLocation();
        }
        else {
            // jika lokasi ditolak
            // memanggil  method
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION }, 100);
        }

        // mengembalikan view
        return view;
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation()
    {
        // insialisasi map fragment
        SupportMapFragment mapFragment=(SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);

        // insialisasi lokasi manager
        LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

        // melihat kondisi
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            client.getLastLocation().addOnCompleteListener(
                    task -> {
                        // insialisasi Lokasi
                        Location location = task.getResult();
                        // melihat kondisi
                        if (location != null) {
                            mapFragment.getMapAsync(googleMap -> {
                                LatLng lokasi = new LatLng(location.getLatitude(),location.getLongitude());
                                MarkerOptions options = new MarkerOptions().position(lokasi).title("Lokasi Anda");
                                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lokasi,17));
                                googleMap.addMarker(options);
                            });
                        }
                        else {
                            LocationRequest locationRequest = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(10000).setFastestInterval(1000).setNumUpdates(1);
                            LocationCallback locationCallback = new LocationCallback() {
                                @Override
                                public void
                                onLocationResult(@NonNull LocationResult locationResult)
                                {
                                    mapFragment.getMapAsync(googleMap -> {
                                        LatLng lokasi = new LatLng(location.getLatitude(),location.getLongitude());
                                        MarkerOptions options = new MarkerOptions().position(lokasi).title("Lokasi Sekarang");
                                        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lokasi,17));
                                        googleMap.addMarker(options);
                                    });
                                }
                            };

                            // Updating Loc
                            client.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                        }
                    });
        }
        else {
            startActivity(
                    new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
}
