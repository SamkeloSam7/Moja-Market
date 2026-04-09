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

public class HomeFragment extends Fragment {

    private RecyclerView itemsRecyclerView;
    private ItemAdapter itemAdapter;
    private List<Item> itemList;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        itemsRecyclerView = view.findViewById(R.id.itemsRecyclerView);
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        itemList = new ArrayList<>();

        itemList.add(new Item(
                1,
                "iPhone 13 Pro",
                "Excellent condition, barely used. Comes with original box and charger.",
                12000,
                "Johannesburg",
                "Used - Excellent",
                1,
                android.R.drawable.ic_menu_gallery,
                4.8,
                5,
                "28/03/2026",
                "yamkela_j"
        ));

        itemList.add(new Item(
                2,
                "Gaming Laptop",
                "High performance laptop suitable for gaming, design, and coding.",
                15000,
                "Johannesburg",
                "Used - Good",
                1,
                android.R.drawable.ic_menu_gallery,
                4.7,
                8,
                "29/03/2026",
                "blessings_k"
        ));

        itemList.add(new Item(
                3,
                "Sony Headphones",
                "Clean audio, noise cancelling, and still in very good condition.",
                2500,
                "Wits Campus",
                "Used - Very Good",
                2,
                android.R.drawable.ic_menu_gallery,
                4.6,
                4,
                "30/03/2026",
                "samkelo_m"
        ));

        itemAdapter = new ItemAdapter(requireContext(), itemList);
        itemsRecyclerView.setAdapter(itemAdapter);
    }
}