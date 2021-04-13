package edu.osu.timekiller;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder>{


    private Context mCtx;
    private List<List> postList;


    //getting the context and product list with constructor
    public PostAdapter(List<List> postList) {

        this.postList = postList;
    }

    @NonNull
    @Override
    public PostAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_postlayout, parent, false);
        PostViewHolder holder = new PostViewHolder(view);
        mCtx = parent.getContext();
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        List post = postList.get(position);
        Log.d("current post", post.toString());
        holder.locationview.setText("Location: " + post.get(0).toString());
        holder.postview.setText(post.get(1).toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Toast.makeText(mCtx, "Click to edit the post", Toast.LENGTH_SHORT).show();
               }
            }
        );
    }

    @Override
    public int getItemCount() {
        return postList.size();
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


class Post{

}