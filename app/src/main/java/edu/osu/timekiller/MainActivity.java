package edu.osu.timekiller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button mShowScroeBoard; // Button to show Top Players Activity
    public static final String TAG = Register.class.getName();

    TextView nickName, email, highScore,resetNickmame;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    Double latitude = 47.6550;
    Double longitude = -122.3080;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nickName = findViewById(R.id.nickName_text);
        email = findViewById(R.id.emailText);
        highScore = findViewById(R.id.high_score);
        resetNickmame = findViewById(R.id.reset_nickname);

        //FirebaseApp.initializeApp(this);

        fAuth = FirebaseAuth.getInstance();
        if(fAuth.getCurrentUser() == null){
            Intent myIntent = new Intent(MainActivity.this, Login.class);
            MainActivity.this.startActivity(myIntent);
        }
        else{
            fStore = FirebaseFirestore.getInstance();
            userId = fAuth.getCurrentUser().getUid();
            DocumentReference documentReference = fStore.collection(("Information")).document(userId);

            if(documentReference == null){
                FirebaseAuth.getInstance().signOut();
                Intent myIntent = new Intent(MainActivity.this, Login.class);
                MainActivity.this.startActivity(myIntent);
                finish();
            }

            Log.d("documentReference", documentReference.toString());

            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
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
                            Toast.makeText(MainActivity.this, "The nickname cannot be empty", Toast.LENGTH_SHORT).show();
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

        // Get the ScoreBoard Button Resource, Create an Intent to start ScoreBoard Activity
        mShowScroeBoard = findViewById(R.id.button_show_score_board);

        mShowScroeBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.button_show_score_board){
                    Intent intent = new Intent(MainActivity.this, ScoreBoard.class);
                    startActivity(intent);
                }
            }
        });
    }



    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        Intent myIntent = new Intent(MainActivity.this, Login.class);
        MainActivity.this.startActivity(myIntent);
        finish();
    }



    }