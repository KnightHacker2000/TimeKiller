package edu.osu.timekiller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewPostFragment extends Fragment {

    private static final String TAG = "newpostfrag";
    private Chip[] mChipArr = new Chip[4];
    private Button mCreatePostButton = null;
    private TextInputEditText titleTextView = null;
    private TextInputEditText contentTextView = null;
    private TextView locationTextView = null;
    private String post_cate = "";
    private Chip mChip = null;
    private int chip_checked_index = -1;
    private LatLng post_location = null;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    String userId = fAuth.getCurrentUser().getUid();
    CollectionReference mCollectionReference = fStore.collection("posts");
    Map<String, Object> updateMap = new HashMap<>();


    public NewPostFragment() {
        // Required empty public constructor
    }

    public static NewPostFragment newInstance() {
        NewPostFragment fragment = new NewPostFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        titleTextView = (TextInputEditText)view.findViewById(R.id.title_textbox);
        contentTextView = (TextInputEditText)view.findViewById(R.id.content_textbox);
        locationTextView = (TextView) view.findViewById(R.id.location_text);
        mCreatePostButton = (Button) view.findViewById(R.id.button_submit_post);

        mChipArr[0] = (Chip) view.findViewById(R.id.sport_chip);
        mChipArr[1] = (Chip) view.findViewById(R.id.trans_chip);
        mChipArr[2] = (Chip) view.findViewById(R.id.acad_chip);
        mChipArr[3] = (Chip) view.findViewById(R.id.enter_chip);

        for (int i = 0; i < mChipArr.length; i++) {
            Log.d(TAG, "set click listener");
            mChipArr[i].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    if (chip_checked_index !=-1 && view.getId() == mChipArr[chip_checked_index].getId()) {
                        mChipArr[chip_checked_index].setChecked(false);
                        chip_checked_index = -1;
                        post_cate = "";
                    } else {
                        if(chip_checked_index != -1){
                            mChipArr[chip_checked_index].setChecked(false);
                        }
                        if (view.getId() == R.id.sport_chip) {
                            chip_checked_index = 0;
                            post_cate = "sport";
                        }
                        if (view.getId() == R.id.trans_chip) {
                            chip_checked_index = 1;
                            post_cate = "transportation";
                        }
                        if (view.getId() == R.id.acad_chip) {
                            chip_checked_index = 2;
                            post_cate = "academic";

                        }
                        if (view.getId() == R.id.enter_chip) {
                            chip_checked_index = 3;
                            post_cate = "entertainment";
                        }
                    }
                }
            });
        }


            // Initialize the AutocompleteSupportFragment.
            AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

            // Specify the types of place data to return.
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

            autocompleteFragment.setPlaceFields(fields);

            // Set up a PlaceSelectionListener to handle the response.
            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(@NotNull Place place) {
                    Log.d(TAG, "Place: " + place.getName() + ", " + place.getId());

                    // Dynamically add the textView
                    locationTextView = view.findViewById(R.id.location_text);
                    int id = View.generateViewId();
                    locationTextView.setText("Event Location: " + place.getName());

                    // Update LatLng
                    post_location = place.getLatLng();
                }

                @Override
                public void onError(@NotNull Status status) {
                    Log.i(TAG, "An error occurred: " + status);
                }
            });

            mCreatePostButton.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    Log.d(TAG,titleTextView.getText().toString());
                    // check for completion
                    if (post_cate == "") {
                        Toast.makeText(getContext(), "Please select post category before submitting", Toast.LENGTH_SHORT).show();
                    } else if (post_cate != "" && titleTextView.getText().toString().length() == 0) {
                        Toast.makeText(getContext(), "Please add title before submitting", Toast.LENGTH_SHORT).show();
                    } else if (post_cate != "" && titleTextView.getText().toString().length() > 0 && contentTextView.getText().toString().length() == 0) {
                        Toast.makeText(getContext(), "Please add description before submitting", Toast.LENGTH_SHORT).show();
                    } else if (post_cate != "" && titleTextView.getText().toString().length() > 0 && contentTextView.getText().toString().length() > 0 && post_location == null) {
                        Toast.makeText(getContext(), "Please select location before submitting", Toast.LENGTH_SHORT).show();
                    } else {

                        updateMap.put("title", titleTextView.getText().toString());
                        updateMap.put("description", contentTextView.getText().toString());
                        updateMap.put("location_lat", post_location.latitude);
                        updateMap.put("location_long", post_location.longitude);
                        updateMap.put("post_category", post_cate);
                        updateMap.put("user_id", userId);

                        mCollectionReference.document().set(updateMap);
                        Toast.makeText(getContext(), "Create Post Success", Toast.LENGTH_SHORT).show();

                        titleTextView.setText("");
                        contentTextView.setText("");
                        locationTextView.setText("Location Not Set");
                        mChipArr[chip_checked_index].setChecked(false);

                        //getActivity().getFragmentManager().beginTransaction().remove(this).commit();
                    }
                }
            });
            return view;
        }



}