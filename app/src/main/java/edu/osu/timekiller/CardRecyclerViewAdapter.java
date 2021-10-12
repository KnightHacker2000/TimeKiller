//package edu.osu.timekiller;
//
//import android.content.Intent;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.content.Context;
//import android.widget.Toast;
//
//
//import java.util.List;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
///**
// * Adapter used to show a simple grid of products.
// */
//public class CardRecyclerViewAdapter extends RecyclerView.Adapter<CardViewHolder> {
//
//    private List<Card> postList;
//    private Context context;
//    CardRecyclerViewAdapter(List<Card> postList) {
//        this.postList = postList;
//    }
//
//    @NonNull
//    @Override
//    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
//        context = parent.getContext();
//        return new CardViewHolder(layoutView);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
//        Card post = postList.get(position);
//        Log.d("current post", post.toString());
//        holder.title.setText("Title: " + post.title);
//        holder.description.setText("Description: "+post.description);
//
////        holder.itemView.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Intent intent = new Intent(context, InfoActivity.class);
////                intent.putExtra("post_title",post.title);
////                context.startActivity(intent);
////            }
////        });
//    }
//
//
//
//    @Override
//    public int getItemCount() {
//        return postList.size();
//    }
//}
//
