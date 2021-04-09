package edu.osu.timekiller;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

public class UserProfileFragment extends Fragment {


    public static final String TAG = Register.class.getName();
    TextView nickName, email, highScore,resetNickmame;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        nickName = view.findViewById(R.id.nickName_text);
        email = view.findViewById(R.id.emailText);
        highScore = view.findViewById(R.id.high_score);
        resetNickmame = view.findViewById(R.id.reset_nickname);

        // From MainActivity

        fAuth = FirebaseAuth.getInstance();
        if(fAuth.getCurrentUser() != null){

            fStore = FirebaseFirestore.getInstance();
            userId = fAuth.getCurrentUser().getUid();
            DocumentReference documentReference = fStore.collection(("Information")).document(userId);


            documentReference.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if(fAuth.getCurrentUser() != null) {
                        email.setText(documentSnapshot.getString("email"));
                        nickName.setText(documentSnapshot.getString("nickname"));
                        highScore.setText(documentSnapshot.getString("high_score"));
                    }
                }
            });
        }


        resetNickmame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("myTag", "This is on click of reset nick name");
                // Store user info into the table called Information
                String currentUID = fAuth.getCurrentUser().getUid();
                DocumentReference documentReference = fStore.collection("Information").document(currentUID);
                Map<String,Object> updateMap = new HashMap<>();

                final EditText resetText = new EditText(view.getContext());
                final AlertDialog.Builder resetDialog = new AlertDialog.Builder(view.getContext());
                resetDialog.setTitle("Do you want to reset nickname ?");
                resetDialog.setMessage("Please enter new nickname");
                resetDialog.setView(resetText);
                resetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newNickname = resetText.getText().toString();

                        if(newNickname.length() <= 0){
                            Toast.makeText(getActivity(), "The nickname cannot be empty", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        updateMap.put("nickname",newNickname);
                        updateMap.put("email",email.getText().toString());
                        updateMap.put("high_score", highScore.getText().toString());
                        documentReference.set(updateMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "Success: nickname changed successfully "+ currentUID);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Failure: " + e.toString());
                            }
                        });
                    }
                });

                resetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Empty for closing
                    }
                });
                resetDialog.create().show();
            }
        });

        return view;
    }

    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        Intent myIntent = new Intent(getActivity(), Login.class);
        startActivity(myIntent);
        //finish();
    }
}