package edu.osu.timekiller;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.*;

public class PostAdapter extends FirestoreRecyclerAdapter<Card, PostAdapter.PostViewHolder> {

    public static final String TAG = Register.class.getName();
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    CollectionReference postdb = fStore.collection("posts");;

    private Context mCtx;
    public PostAdapter(@NonNull FirestoreRecyclerOptions<Card> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull Card model) {
        holder.locationview.setText("Location: " + model.getPlace_name());
        holder.postview.setText(model.getDescription());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {

                   final EditText resetText = new EditText(view.getContext());
                   final AlertDialog.Builder resetDialog = new AlertDialog.Builder(view.getContext());
                   resetDialog.setTitle("Do you change your post context");
                   resetDialog.setMessage("Please enter the edit context");
                   resetDialog.setView(resetText);
                   resetDialog.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                           String newContent = resetText.getText().toString();

                           if(newContent.length() <= 0){
                               Toast.makeText(mCtx, "The context cannot be empty", Toast.LENGTH_SHORT).show();
                               return;
                           }

                           String newPostContent = resetText.getText().toString();
                           //Need a post id to edit
                           postdb.whereEqualTo("post_id",model.getPost_id()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                               @Override
                               public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                   if (task.isSuccessful()) {
                                       for (QueryDocumentSnapshot document : task.getResult()) {
                                           Map<String,Object> updateMap = new HashMap<>();
                                           updateMap.put("description", newPostContent);
                                           postdb.document(document.getId()).set(updateMap, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                               @Override
                                               public void onSuccess(Void aVoid) {
                                                   Log.d(TAG, "Success: post content changed successfully "+ document.getId().toString());
                                               }
                                           }).addOnFailureListener(new OnFailureListener() {
                                               @Override
                                               public void onFailure(@NonNull Exception e) {
                                                   Log.d(TAG, "Failure: " + e.toString());
                                               }
                                           });
                                       }
                                   }
                               }
                           });
                       }
                   });

                   resetDialog.setNeutralButton("Delete Post", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                           postdb.document(model.getPost_id().toString()).delete()
                           .addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void aVoid) {
                                   Toast.makeText(mCtx, "You successfully deleted this post", Toast.LENGTH_LONG).show();
                               }

                           }).addOnFailureListener(new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {
                                   Log.d(TAG, "Failure: " + e.toString());
                               }
                           });
                           // Empty for closing
                       }
                   });

                   resetDialog.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                           // Empty for closing
                       }
                   });
                   resetDialog.create().show();

               }
           }
        );
    }



    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_postlayout, parent, false);
        PostViewHolder holder = new PostViewHolder(view);
        mCtx = parent.getContext();
        return holder;

    }



    class PostViewHolder extends RecyclerView.ViewHolder{
        TextView postview, locationview;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            locationview = itemView.findViewById(R.id.location_context_text);
            postview = itemView.findViewById(R.id.post_context_text);
        }
    }

}

