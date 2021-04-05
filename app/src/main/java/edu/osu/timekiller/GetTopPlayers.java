package edu.osu.timekiller;

        import android.annotation.SuppressLint;
        import android.content.Context;
        import android.location.Location;
        import android.os.Build;
        import android.util.Log;
        import android.util.Pair;

        import androidx.annotation.NonNull;
        import androidx.annotation.RequiresApi;

        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.LatLngBounds;
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.firestore.DocumentReference;
        import com.google.firebase.firestore.DocumentSnapshot;
        import com.google.firebase.firestore.FirebaseFirestore;
        import com.google.firebase.firestore.QueryDocumentSnapshot;
        import com.google.firebase.firestore.QuerySnapshot;
        import com.google.firebase.firestore.model.Document;

        import java.util.ArrayList;
        import java.util.Comparator;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

public class GetTopPlayers {


    private static final String TAG = "LocationService";

    @SuppressLint("NewApi")
    public static List<Pair<LatLng, Integer>> getTopPlayers(LatLngBounds viewBound, Location lastKnownLocation, Context context) {

        String[] userAddress = UpdateUserLocation.getUserAddress(context, lastKnownLocation);

        List<Pair<LatLng, Integer>> scoreList = new ArrayList<>();


        List<DocumentSnapshot> docs = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Information")
                .whereEqualTo("country", userAddress[0])
                .whereEqualTo("subLocality", userAddress[1])
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Document "+" is received.");

                    int i=0;
                    for (DocumentSnapshot document : task.getResult()) {
                        //Log.d(TAG, "Document "+i+" is received.");

                        docs.add(document);
                    }
                } else{
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        Log.d("LocationService","list size = "+docs.size());


        for(DocumentSnapshot document: docs) {
            LatLng playerLocale = new LatLng(document.getDouble("latitude"), document.getDouble("longitude"));
            Log.d("LocationService", "query success!");

            if (viewBound.contains(playerLocale)) {
                //Log.d("LocationService", "query success!");

                Pair<LatLng, Integer> pair = new Pair<>(playerLocale, Integer.valueOf(document.getString("high_score")));
                Log.d("LocationService", "score = " + document.getString("high_score"));

                scoreList.add(pair);
            }
        }
        //Log.d("LocationService","list size = "+scoreList.size());


        scoreList.sort(new Comparator<Pair<LatLng, Integer>>() {
            @Override
            public int compare(Pair<LatLng, Integer> t1, Pair<LatLng, Integer> t2) {
                return t1.second.compareTo(t2.second);
            }
        });


        return scoreList;

    }
}
