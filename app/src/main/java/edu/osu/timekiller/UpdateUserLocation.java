package edu.osu.timekiller;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateUserLocation {


    private static final String TAG = "UserLocationClass";

    public static void updateUserLocation(Location location, Context context) {

        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        String userId = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("Information").document(userId);
        Map<String, Object> updateMap = new HashMap<>();
        Geocoder geocoder = new Geocoder(context);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "Document exists");
                        String[] userAddress = getUserAddress(context,location);


                        updateMap.put("email", document.getString("email"));
                        updateMap.put("nickname", document.getString("nickname"));
                        updateMap.put("high_score", document.getString("high_score"));
                        updateMap.put("latitude", location.getLatitude());
                        updateMap.put("longitude", location.getLongitude());
                        updateMap.put("country", userAddress[0]);
                        updateMap.put("subLocality", userAddress[1]);


                        documentReference.set(updateMap)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully written!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error writing document", e);
                                    }
                                });
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public static String[] getUserAddress(Context context, Location location){
        String[] userAdd = new String[3];
        Geocoder geocoder = new Geocoder(context);


        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            if (geocoder.isPresent()) {
                StringBuilder stringBuilder = new StringBuilder();
                if (addresses.size()>0) {
                    Address returnAddress = addresses.get(0);

                    userAdd[1] = returnAddress.getSubLocality();
                    userAdd[0]=returnAddress.getCountryName();
                    userAdd[2] = returnAddress.getPostalCode();

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return userAdd;
    }





}
