package com.example.mojamarket;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder> {

    private final List<OnboardingItem> onboardingItems;

    public OnboardingAdapter(List<OnboardingItem> onboardingItems) {
        this.onboardingItems = onboardingItems;
    }

    @NonNull
    @Override
    public OnboardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_onboarding, parent, false);
        return new OnboardingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnboardingViewHolder holder, int position) {
        OnboardingItem item = onboardingItems.get(position);

        holder.slideIcon.setImageResource(item.getIconResId());
        holder.slideTitle.setText(item.getTitle());
        holder.slideDescription.setText(item.getDescription());
        holder.iconBackground.setBackgroundResource(item.getBackgroundResId());
    }

    @Override
    public int getItemCount() {
        return onboardingItems.size();
    }

    static class OnboardingViewHolder extends RecyclerView.ViewHolder {
        ImageView slideIcon;
        TextView slideTitle;
        TextView slideDescription;
        FrameLayout iconBackground;

        public OnboardingViewHolder(@NonNull View itemView) {
            super(itemView);
            slideIcon = itemView.findViewById(R.id.slideIcon);
            slideTitle = itemView.findViewById(R.id.slideTitle);
            slideDescription = itemView.findViewById(R.id.slideDescription);
            iconBackground = itemView.findViewById(R.id.iconBackground);
        }
    }
}