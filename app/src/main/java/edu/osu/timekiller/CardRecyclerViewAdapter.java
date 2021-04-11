package edu.osu.timekiller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Adapter used to show a simple grid of products.
 */
public class CardRecyclerViewAdapter extends RecyclerView.Adapter<CardViewHolder> {

//    private List<ProductEntry> productList;
//    private ImageRequester imageRequester;

    CardRecyclerViewAdapter() {
//        this.productList = productList;
//        imageRequester = ImageRequester.getInstance();
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new CardViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {

    }



    @Override
    public int getItemCount() {
//        return productList.size();
        return 0;
    }
}

