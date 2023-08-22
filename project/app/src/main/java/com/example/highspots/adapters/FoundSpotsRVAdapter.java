package com.example.highspots.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.highspots.R;
import com.example.highspots.interfaces.FoundSpotClickListener;
import com.example.highspots.models.Spot;

import java.util.List;

public class FoundSpotsRVAdapter extends RecyclerView.Adapter<FoundSpotsRVAdapter.ViewHolder> {

    private List<Spot> foundSpots;
    private FoundSpotClickListener listener;

    public FoundSpotsRVAdapter(List<Spot> foundSpots, FoundSpotClickListener listener) {
        this.foundSpots = foundSpots;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View spotView = inflater.inflate(R.layout.found_spot_item_rv, parent, false);

        FoundSpotsRVAdapter.ViewHolder viewHolder = new FoundSpotsRVAdapter.ViewHolder(spotView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.ratingTV.setText("Rating: " + String.format("%.2f", foundSpots.get(position).getRating()));
        holder.visitorsTV.setText("Visitors: " + foundSpots.get(position).getVisitors().size());
        holder.imageView.setImageResource(R.drawable.found_spot_icon);
    }

    @Override
    public int getItemCount() {
        return foundSpots.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView ratingTV;
        private final TextView visitorsTV;
        private final ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            this.ratingTV = view.findViewById(R.id.foundSpotsItemRatingTV);
            this.visitorsTV = view.findViewById(R.id.foundSpotsItemVisitorsTV);
            this.imageView = view.findViewById(R.id.featureIV);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onFoundSpotClick(foundSpots.get(position));
                    }

                }
            });
        }
    }
}
