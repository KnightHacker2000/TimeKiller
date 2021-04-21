package edu.osu.timekiller;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.zip.Inflater;


class post{

}
public class UserProfileFragment extends Fragment {


    public static final String TAG = Register.class.getName();
    private FirestoreRecyclerAdapter updateAdapter;
    TextView nickName, email,resetNickmame,post;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    DatabaseReference dbPost;
    String userId;
    RecyclerView recyclerView;
    PostAdapter adapter;
    List<Card> postList = new ArrayList<>();


    DocumentReference documentReference;
    CollectionReference postdb;
    String description_data;

    public UserProfileFragment() {
        // Required empty public constructor
    }


    public void onCreate(Bundle savedInstanceState) {
        fStore = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        fAuth = FirebaseAuth.getInstance();
        if(fAuth.getCurrentUser() != null){
            userId = fAuth.getCurrentUser().getUid();
            documentReference = fStore.collection(("Information")).document(userId);
            postdb = fStore.collection("posts");
            dbPost = FirebaseDatabase.getInstance().getReference("posts");
        }

        postdb.whereEqualTo("user_id",fAuth.getCurrentUser().getUid().toString()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                String location_data = "";
                String description_data = "";
                postList = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                    String location = documentSnapshot.getString("place_name");
                    String contenxt = documentSnapshot.getString("description");
                    String title = documentSnapshot.getString("title");
                    String post_id = documentSnapshot.getString("post_id");
                    Card tmp = new Card(title, contenxt, location, post_id);
                    postList.add(tmp);
                }
            }
        });

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        Query query = fStore.collection("posts").whereEqualTo("user_id",fAuth.getCurrentUser().getUid().toString());
        FirestoreRecyclerOptions<Card> options = new FirestoreRecyclerOptions.Builder<Card>()
                .setQuery(query, Card.class)
                .build();
        adapter = new PostAdapter(options);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);



        nickName = view.findViewById(R.id.nickName_text);
        email = view.findViewById(R.id.emailText);
        resetNickmame = view.findViewById(R.id.reset_nickname);
//        query = FirebaseDatabase.getInstance().getReference("posts")
//                .orderByChild("user_id")
//                .equalTo("chenqiheng003@163.com");

        // From MainActivity


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
                        updateMap.put("user_id",fAuth.getCurrentUser().getUid().toString());
                        nickName.setText(newNickname);

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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                email.setText(documentSnapshot.getString("email"));
                nickName.setText(documentSnapshot.getString("nickname"));

            }

        });



//        postdb.whereEqualTo("user_id","FUaLAmb26paz7iI0UpJRuxVD8hZ2").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                String data = "";
//                for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
//                    data = documentSnapshot.get("description").toString();
//                    post.setText(data);
//                }
//
//            }
//        });

    }

    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        Intent myIntent = new Intent(getActivity(), Login.class);
        startActivity(myIntent);
        //finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}