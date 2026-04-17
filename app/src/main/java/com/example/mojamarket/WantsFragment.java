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

import com.example.mojamarket.models.Want;
import com.example.mojamarket.utility.WantDatabase;

public class WantsFragment extends Fragment {

    private RecyclerView wantsRecyclerView;
    private MyWantAdapter wantRequestAdapter;
    private List<Want> requestList;

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
        requestList = WantDatabase.getWants(requireContext());

        wantRequestAdapter = new MyWantAdapter(requireContext(), requestList);
        wantsRecyclerView.setAdapter(wantRequestAdapter);
    }
}