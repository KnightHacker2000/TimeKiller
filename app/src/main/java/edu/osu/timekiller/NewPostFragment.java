package edu.osu.timekiller;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewPostFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "newpostfrag";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewPostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostCategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewPostFragment newInstance(String param1, String param2) {
        NewPostFragment fragment = new NewPostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);


//        Button mButton = view.findViewById(R.id.location_button);
//        mButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
//                        .build(getContext());
//                startActivityForResult(intent, 1);
//            }
//        });

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NotNull Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());

                // Dynamically add the textView
                TextView textView = view.findViewById(R.id.location_text);
                int id = View.generateViewId();
                textView.setText("Event Location: "+place.getName());

//                textView.setTextSize(25);
//                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
//                        ConstraintLayout.LayoutParams.MATCH_PARENT,
//                        ConstraintLayout.LayoutParams.MATCH_PARENT
//                );
//                ConstraintSet constraintSet = new ConstraintSet();
//                constraintSet.clone(clmain);
//                constraintSet.connect(textView.getId(), ConstraintSet.TOP, R.id.autocomplete_fragment, ConstraintSet.BOTTOM, 10);
//                constraintSet.connect(textView.getId(), ConstraintSet.LEFT, clmain.getId(), ConstraintSet.LEFT, 20);
//
//                textView.setLayoutParams(params);
//                constraintSet.applyTo(clmain);
//                clmain.addView(textView);

            }


            @Override
            public void onError(@NotNull Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        return view;
    }

 //   @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == 1) {
//            if (resultCode == RESULT_OK) {
//                Place place = Autocomplete.getPlaceFromIntent(data);
//                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
//
//                ConstraintLayout clmain = getView().findViewById(R.id.layout);
//                TextView textView = new TextView(getContext());
//                int id = View.generateViewId();
//                textView.setId(id);
//                textView.setText("Event Location: "+place.getName());
//                textView.setTextSize(25);
//                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
//                        ConstraintLayout.LayoutParams.MATCH_PARENT,
//                        ConstraintLayout.LayoutParams.MATCH_PARENT
//                );
//                ConstraintSet constraintSet = new ConstraintSet();
//                constraintSet.clone(clmain);
//                constraintSet.connect(textView.getId(), ConstraintSet.TOP, R.id.location_button, ConstraintSet.BOTTOM, 10);
//                constraintSet.connect(textView.getId(), ConstraintSet.LEFT, clmain.getId(), ConstraintSet.LEFT, 20);
//
//                textView.setLayoutParams(params);
//                constraintSet.applyTo(clmain);
//                clmain.addView(textView);
//            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
//                // TODO: Handle the error.
//                Status status = Autocomplete.getStatusFromIntent(data);
//                Log.i(TAG, status.getStatusMessage());
//            } else if (resultCode == RESULT_CANCELED) {
//                // The user canceled the operation.
//            }
//            return;
//        }
//    }

}