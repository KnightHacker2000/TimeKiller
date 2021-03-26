///**
// * Fragment Class for a Top Player Map View
// *     TODO: Access User Locations, Show Highest Score On Map
// */
//package edu.osu.timekiller;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.core.app.ActivityCompat;
//import androidx.fragment.app.Fragment;
//
//import android.Manifest;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.location.Location;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//public class MapViewFragment extends Fragment
//        implements
//        GoogleMap.OnMyLocationButtonClickListener,
//        GoogleMap.OnMyLocationClickListener,
//        OnMapReadyCallback,
//        ActivityCompat.OnRequestPermissionsResultCallback {
//
//    private OnMapReadyCallback callback = new OnMapReadyCallback() {
//
//        /**
//         * Request code for location permission request.
//         *
//         * @see #onRequestPermissionsResult(int, String[], int[])
//         */
//        private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
//
//        /**
//         * Flag indicating whether a requested permission has been denied after returning in
//         * {@link #onRequestPermissionsResult(int, String[], int[])}.
//         */
//        private boolean permissionDenied = false;
//
//
//        /**
//         * Manipulates the map once available.
//         * This callback is triggered when the map is ready to be used.
//         * This is where we can add markers or lines, add listeners or move the camera.
//         * In this case, we just add a marker near Sydney, Australia.
//         * If Google Play services is not installed on the device, the user will be prompted to
//         * install it inside the SupportMapFragment. This method will only be triggered once the
//         * user has installed Google Play services and returned to the app.
//         */
//        @Override
//        public void onMapReady(GoogleMap map) {
//            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // Permission to access the location is missing. Show rationale and request permission
//                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},LOCATION_PERMISSION_REQUEST_CODE);
//
//            } else{
//                if (map != null) {
//                    map.setMyLocationEnabled(true);
//                }
//            }
//            map.setMyLocationEnabled(true);
//            map.setOnMyLocationButtonClickListener(this);
//            map.setOnMyLocationClickListener(this);
//            enableMyLocation(map);
//
////            LatLng sydney = new LatLng(47.6550, -122.3080);
////            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
////            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        }
//
//        private void enableMyLocation(GoogleMap map){
//            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // Permission to access the location is missing. Show rationale and request permission
//                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},LOCATION_PERMISSION_REQUEST_CODE);
//
//            } else{
//                if (map != null) {
//                    map.setMyLocationEnabled(true);
//                }
//            }
//        }
//    };
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_maps, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        SupportMapFragment mapFragment =
//                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
//        if (mapFragment != null) {
//            mapFragment.getMapAsync(callback);
//        }
//    }
//
//    @Override
//    public boolean onMyLocationButtonClick() {
//        return false;
//    }
//
//    @Override
//    public void onMyLocationClick(@NonNull Location location) {
//        Toast.makeText(getActivity(), "Current location:\n" + location, Toast.LENGTH_LONG).show();
//
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
//            return;
//        }
//
//        if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {
//            // Enable the my location layer if the permission has been granted.
//            enableMyLocation();
//        } else {
//            // Permission was denied. Display an error message
//            // Display the missing permission error dialog when the fragments resume.
//            permissionDenied = true;
//        }
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//
//    }
//}