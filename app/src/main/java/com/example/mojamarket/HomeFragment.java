package com.example.mojamarket;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mojamarket.models.Post;
import com.example.mojamarket.network.PostRepository;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView itemsRecyclerView;
    private ItemAdapter itemAdapter;
    private List<Post> posts;

    public HomeFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        itemsRecyclerView = view.findViewById(R.id.itemsRecyclerView);
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        posts = new ArrayList<>();
        itemAdapter = new ItemAdapter(requireContext(), posts);
        itemsRecyclerView.setAdapter(itemAdapter);
        loadFeed();
    }

    private void loadFeed() {
        PostRepository.getFeed(new PostRepository.PostsCallback() {
            @Override
            public void onSuccess(List<Post> result) {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    posts.clear();
                    posts.addAll(result);
                    itemAdapter.notifyDataSetChanged();
                    if (result.isEmpty()) {
                        Toast.makeText(requireContext(), "No posts found", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(String message) {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    Log.e("HomeFragment", "Feed error: " + message);
                    Toast.makeText(requireContext(), "Failed to load feed: " + message, Toast.LENGTH_LONG).show();
                });
            }
        });
    }
}