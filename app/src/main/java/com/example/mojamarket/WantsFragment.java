package com.example.mojamarket;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mojamarket.models.Want;
import com.example.mojamarket.network.WantRepository;

import java.util.ArrayList;
import java.util.List;

public class WantsFragment extends Fragment {

    private RecyclerView wantsRecyclerView;
    private WantRequestAdapter wantRequestAdapter;
    private List<Want> requestList;

    public WantsFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wants, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        wantsRecyclerView = view.findViewById(R.id.wantsRecyclerView);
        wantsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        requestList = new ArrayList<>();
        wantRequestAdapter = new WantRequestAdapter(requireContext(), requestList);
        wantsRecyclerView.setAdapter(wantRequestAdapter);
        loadWantsFeed();
    }

    private void loadWantsFeed() {
        WantRepository.getFeed(new WantRepository.WantsCallback() {
            @Override
            public void onSuccess(List<Want> result) {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    requestList.clear();
                    requestList.addAll(result);
                    wantRequestAdapter.notifyDataSetChanged();
                });
            }

            @Override
            public void onFailure(String message) {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "Failed to load wants: " + message, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}