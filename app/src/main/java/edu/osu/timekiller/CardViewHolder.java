package edu.osu.timekiller;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CardViewHolder  extends RecyclerView.ViewHolder {
    TextView title, description;
    public CardViewHolder(@NonNull View itemView) {
        super(itemView);
       title = itemView.findViewById(R.id.post_title);
       description = itemView.findViewById(R.id.post_description);


    }

}
