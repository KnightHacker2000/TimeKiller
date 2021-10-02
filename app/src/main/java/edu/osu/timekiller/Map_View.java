package edu.osu.timekiller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Map_View#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Map_View extends Fragment {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int DEFAULT_ZOOM = 15;
    private MapView mMapView = null;
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationClient = null;
    private boolean locationPermissionGranted = false;
    private Location lastKnownLocation;
    private static final String TAG = "LocationService";


    public Map_View() {
        // Required empty public constructor
    }

    public static Map_View newInstance() {
        Map_View fragment = new Map_View();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, null, false);
        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                getDeviceLocation();
                googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {
                        Toast.makeText(getContext(), "Locating", Toast.LENGTH_LONG)
                                .show();
                        getDeviceLocation();
                        // Return false so that we don't consume the event and the default behavior still occurs
                        // (the camera animates to the user's current position).
                        return false;
                    }
                });
                googleMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
                    @Override
                    public void onMyLocationClick(@NonNull Location location) {
                        Toast.makeText(getContext(), "Current location:\n" + location, Toast.LENGTH_LONG)
                                .show();
                    }
                });
                enableMyLocation();
                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        String userId = marker.getSnippet();
                        Intent intent = new Intent(getActivity(), InfoActivity.class);
                        intent.putExtra("post_id",marker.getSnippet());
                        startActivity(intent);
                        return true;
                    }
                });

            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                LatLng position = new LatLng(document.getDouble("location_lat"), document.getDouble("location_long"));
                                int cate_res = -1;
                                if(document.getString("post_category") == "sport"){
                                    cate_res = R.drawable.outline_fitness_center_24;
                                } else if (document.getString("post_category") == "transportation"){
                                    cate_res = R.drawable.outline_drive_eta_24;
                                } else if (document.getString("post_category") == "academic"){
                                    cate_res = R.drawable.outline_auto_stories_24;
                                }  else if (document.getString("post_category") == "entertainment"){
                                    cate_res = R.drawable.outline_attractions_24;
                                }

                                //BitmapDescriptor icon = BitmapFromVector(getContext(),cate_res);

                                googleMap.addMarker(new MarkerOptions()
                                        .position(position)
                                        .snippet(document.getId())
                                );
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing. Show rationale and request permission
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            if (googleMap != null) {
                googleMap.setMyLocationEnabled(true);
            }
        }
    }

    private void getLocationPermission() {

        if (ContextCompat.checkSelfPermission((Activity)requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
            Log.d(TAG, "Location Permission Granted");
        } else {
            ActivityCompat.requestPermissions((Activity)requireActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            ActivityCompat.requestPermissions((Activity)requireActivity(),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
        }
    }



    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        Log.d(TAG, "getDeviceLocationRunning");
        getLocationPermission();
        try {
            if (locationPermissionGranted) {
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
                Task<Location> locationResult = fusedLocationClient.getLastLocation();
                locationResult.addOnCompleteListener(requireActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
//                            Log.d(TAG,"Last Location:"+lastKnownLocation.getLatitude()+","+
//                                    lastKnownLocation.getLongitude());
                            if (lastKnownLocation != null) {

                                UpdateUserLocation.updateUserLocation(lastKnownLocation, getContext());
                                Log.d(TAG, "Location not Null!");
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), DEFAULT_ZOOM));


                                    showTopPlayers();


                            }

                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            googleMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(new LatLng(47.6550, -122.3080), DEFAULT_ZOOM));
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }

    }

    public void showTopPlayers() {

        VisibleRegion viewPort = googleMap.getProjection().getVisibleRegion();
        LatLngBounds viewBound = viewPort.latLngBounds;

        LatLng northeast = viewBound.northeast; // test bound
        LatLng southwest = viewBound.southwest; // test bound


        //List<Pair<LatLng, Integer>> scoreList = GetTopPlayers.getTopPlayers(viewBound, lastKnownLocation, getContext());
        //Log.d(TAG,"list size = "+scoreList.size());

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                LatLng position = new LatLng(document.getDouble("location_lat"), document.getDouble("location_long"));
                                int cate_res = -1;
                                if(document.getString("post_category") == "sport"){
                                    cate_res = R.drawable.outline_fitness_center_24;
                                } else if (document.getString("post_category") == "transportation"){
                                    cate_res = R.drawable.outline_drive_eta_24;
                                } else if (document.getString("post_category") == "academic"){
                                    cate_res = R.drawable.outline_auto_stories_24;
                                }  else if (document.getString("post_category") == "entertainment"){
                                    cate_res = R.drawable.outline_attractions_24;
                                }

                                //BitmapDescriptor icon = BitmapFromVector(getContext(),cate_res);

                                googleMap.addMarker(new MarkerOptions()
                                        .position(position)
                                        .snippet(document.getId())
                                );
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}