package com.example.gamefit.activity_list;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gamefit.R;
import com.example.gamefit.activity_gps.StartActivity;
import com.example.gamefit.activity_workout.WorkoutActivity;

import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder> {

    private List<ActivityItem> activityList;
    private onItemClickListener listener;
    private Context context;

    public interface onItemClickListener {
        void onItemClick(ActivityItem item);
    }

    public ActivityAdapter(Context context, List<ActivityItem> activityList, onItemClickListener listener) {
        this.context = context;
        this.activityList = activityList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity_card, parent, false);
        return new ActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        ActivityItem currentItem = activityList.get(position);
        holder.bind(currentItem, listener, context);
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    public static class ActivityViewHolder extends RecyclerView.ViewHolder {
        public ImageView iconDistance, iconActivity, iconDiamond, iconDifficulty;
        public TextView tvDistance, tvActivity, tvDiamond, tvDifficulty;
        public Button btnStart;

        public ActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            iconDistance = itemView.findViewById(R.id.iconDistance);
            iconActivity = itemView.findViewById(R.id.iconActivity);
            iconDiamond = itemView.findViewById(R.id.iconDiamond);
            iconDifficulty = itemView.findViewById(R.id.iconDifficulty);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            tvActivity = itemView.findViewById(R.id.tvActivity);
            tvDiamond = itemView.findViewById(R.id.tvDiamond);
            tvDifficulty = itemView.findViewById(R.id.tvDifficulty);
            btnStart = itemView.findViewById(R.id.btnStart);
        }

        public void bind(ActivityItem item, onItemClickListener listener, Context context) {
            // Menggunakan Glide untuk memuat gambar dari URL
            Glide.with(context)
                    .load(item.getDistanceIconUrl()) // Menggunakan URL gambar dari model ActivityItem
                    .into(iconDistance);

            Glide.with(context)
                    .load(item.getActivityIconUrl())
                    .into(iconActivity);

            Glide.with(context)
                    .load(item.getDiamondsIconUrl())
                    .into(iconDiamond);

            Glide.with(context)
                    .load(item.getDifficultyIconUrl())
                    .into(iconDifficulty);

            tvDistance.setText(item.getDistance());
            tvActivity.setText(item.getActivity());
            tvDiamond.setText(String.valueOf(item.getDiamonds()));
            tvDifficulty.setText(item.getDifficulty());

            btnStart.setOnClickListener(v -> {
                listener.onItemClick(item);
                Intent intent = new Intent(context, StartActivity.class);
                context.startActivity(intent);
            });
        }
    }
}

