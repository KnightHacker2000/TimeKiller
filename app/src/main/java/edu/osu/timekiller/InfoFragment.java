package edu.osu.timekiller;

import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoFragment extends Fragment {

    private String PID = "";
    private ImageView mImageView = null;
    private TextView mTitleView = null;
    private TextView mLocationView = null;
    private TextView mDescriptionView = null;
    private TextView mContactView = null;
    private Button mButton = null;
    private Chip mChip = null;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String userId = fAuth.getCurrentUser().getUid();

    private final String TAG="infofrag";


    public InfoFragment() {
        // Required empty public constructor
    }

    public static InfoFragment newInstance() {
        InfoFragment fragment = new InfoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            PID = getArguments().getString("postId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        mImageView = view.findViewById(R.id.place_image);
        mTitleView = view.findViewById(R.id.titleField);
        mChip = view.findViewById(R.id.chip);
        mDescriptionView = view.findViewById(R.id.descriptionField);
        mLocationView = view.findViewById(R.id.location_text);
        mContactView = view.findViewById(R.id.contactInfo);
        mButton = view.findViewById(R.id.button_join);


        DocumentReference doc = fStore.collection("posts").document(PID);

        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    PlacesClient placesClient = Places.createClient(getContext());
                    String placeId = document.getString("place_id");

                    mTitleView.setText(document.getString("title"));
                    mDescriptionView.setText(document.getString("description"));
                    mLocationView.setText(document.getString("place_name"));
                    String post_cate = document.getString("post_category");
                    if(post_cate == "sport"){
                        mChip.setChipIconResource(R.drawable.outline_fitness_center_24);
                        mChip.setText("Sport");
                    } else if(post_cate == "transportation"){
                        mChip.setChipIconResource(R.drawable.outline_drive_eta_24);
                        mChip.setText("Transportation");

                    } else if(post_cate == "academic"){
                        mChip.setChipIconResource(R.drawable.outline_auto_stories_24);
                        mChip.setText("Academic");

                    }else if(post_cate == "entertainment"){
                        mChip.setChipIconResource(R.drawable.outline_attractions_24);
                        mChip.setText("Entertainment");

                    }
                    List<String> participants = (List<String>) document.get("participants");
                    String email = document.getString("contact_email");
                    String name = document.getString("contact_name");
                    if(participants.contains(userId)){
                        mButton.setEnabled(false);
                        mContactView.setVisibility(View.VISIBLE);
                        mContactView.setText("nickname: "+name+", Email: "+email);
                    } else{
                        mButton.setEnabled(true);
                        mContactView.setVisibility(View.INVISIBLE);
                    }
                    //mContactView


                    final List<Place.Field> fields = Collections.singletonList(Place.Field.PHOTO_METADATAS);

                    final FetchPlaceRequest placeRequest = FetchPlaceRequest.newInstance(placeId, fields);



                    placesClient.fetchPlace(placeRequest).addOnSuccessListener((response) -> {
                        final Place place = response.getPlace();

                        // Get the photo metadata.
                        final List<PhotoMetadata> metadata = place.getPhotoMetadatas();
                        if (metadata == null || metadata.isEmpty()) {
                            return;
                        }
                        final PhotoMetadata photoMetadata = metadata.get(0);

                        // Get the attribution text.
                        final String attributions = photoMetadata.getAttributions();

                        // Create a FetchPhotoRequest.
                        final FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                                .build();
                        placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                            Bitmap bitmap = fetchPhotoResponse.getBitmap();
                            mImageView.setImageBitmap(bitmap);
                        }).addOnFailureListener((exception) -> {
                            if (exception instanceof ApiException) {
                                final ApiException apiException = (ApiException) exception;
                                Log.e(TAG, "Place not found: " + exception.getMessage());
                                final int statusCode = apiException.getStatusCode();
                                // TODO: Handle error with given status code.
                            }
                        });
                    });

                }
            }
        });


        return view;
    }
}