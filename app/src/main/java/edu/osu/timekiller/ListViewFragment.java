package edu.osu.timekiller;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListViewFragment extends Fragment {


    private  List<Card> postList = new ArrayList<>();
    private FirebaseFirestore fStore;
    private FirestoreRecyclerAdapter adapter;
    public ListViewFragment() {

    }

    public static ListViewFragment newInstance() {
        ListViewFragment fragment = new ListViewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Query query = fStore.collection("posts");
        //CollectionReference postdb  = fStore.collection("posts");
//        postdb.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                String title = "";
//                String description = "";
//                postList = new ArrayList<>();
//                for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
//                    title = documentSnapshot.get("title").toString(); //wrap in objests.nonnul
//                    description = documentSnapshot.get("description").toString();
//                    postList.add(new Card(title,description));
//                    // Data change
//
//                }Log.i("List View Loading:","now");
//            }
//
//        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Set up the RecyclerView
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);

        fStore = FirebaseFirestore.getInstance();
        Query query = fStore.collection("posts");
        FirestoreRecyclerOptions<Card> options = new FirestoreRecyclerOptions.Builder<Card>().setQuery(query, Card.class).build();
        adapter = new FirestoreRecyclerAdapter<Card, CardViewHolder>(options) {
            @NonNull
            @Override
            public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view,parent,false);
                return new CardViewHolder(view1);
            }

            @Override
            protected void onBindViewHolder(@NonNull CardViewHolder holder, int position, @NonNull Card model) {
                holder.title.setText(model.getTitle());
                holder.description.setText(model.getDescription());
            }

        };



        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));


//        CardRecyclerViewAdapter adapter = new CardRecyclerViewAdapter(postList);
        recyclerView.setAdapter(adapter);
//        int largePadding = getResources().getDimensionPixelSize(R.dimen.grid_spacing);
//        int smallPadding = getResources().getDimensionPixelSize(R.dimen.grid_spacing_small);
//        recyclerView.addItemDecoration(new GridItemDecoration(largePadding, smallPadding));
        return view;
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