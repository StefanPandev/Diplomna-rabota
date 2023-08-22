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
import com.example.highspots.enums.Feature;

import java.util.List;

public class FeatureRVAdapter extends RecyclerView.Adapter<FeatureRVAdapter.ViewHolder> {

    private List<String> features;

    public FeatureRVAdapter(List<String> features) {
        this.features = features;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View spotView = inflater.inflate(R.layout.feature_item_rv, parent, false);

        FeatureRVAdapter.ViewHolder viewHolder = new FeatureRVAdapter.ViewHolder(spotView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.featureName.setText(features.get(position));

        switch (features.get(position)) {
//            case "":
//                break;
            default:
                holder.featureIcon.setImageResource(R.drawable.icon_sunset);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return features.size();
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView featureIcon;
        private final TextView featureName;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            this.featureIcon = view.findViewById(R.id.featureIV);
            this.featureName = view.findViewById(R.id.featureName);
        }
    }
}
