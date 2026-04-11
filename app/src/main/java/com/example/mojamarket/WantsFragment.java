package com.example.mojamarket;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class WantsFragment extends Fragment {

    private RecyclerView wantsRecyclerView;
    private WantRequestAdapter wantRequestAdapter;
    private List<WantRequest> requestList;

    public WantsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wants, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        wantsRecyclerView = view.findViewById(R.id.wantsRecyclerView);
        wantsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        requestList = new ArrayList<>();

        requestList.add(new WantRequest(
                1,
                "iPad 9th Gen",
                "Looking for one in good condition for school work and notes.",
                8000,
                "2 days ago",
                "yamkela_j",
                false,
                false
        ));

        requestList.add(new WantRequest(
                2,
                "Office Chair",
                "Need a comfortable office chair for studying at home.",
                1500,
                "Yesterday",
                "blessings_k",
                false,
                false
        ));

        requestList.add(new WantRequest(
                3,
                "Samsung Tablet",
                "Needed mainly for reading PDFs and online classes.",
                5000,
                "5 days ago",
                "samkelo_m",
                true,
                true
        ));

        wantRequestAdapter = new WantRequestAdapter(requireContext(), requestList);
        wantsRecyclerView.setAdapter(wantRequestAdapter);
    }
}