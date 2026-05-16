package com.example.mojamarket;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mojamarket.models.Post;
import com.example.mojamarket.models.User;
import com.example.mojamarket.models.Want;
import com.example.mojamarket.network.PostRepository;
import com.example.mojamarket.network.UserRepository;
import com.example.mojamarket.session.SessionManager;
import com.example.mojamarket.utility.PostDatabase;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private MaterialButton tabMyListings;
    private MaterialButton tabMyWants;
    private MaterialButton settingsButton;

    private LinearLayout listingsSection;
    private LinearLayout wantsSection;
    private LinearLayout listingsEmptyState;
    private LinearLayout wantsEmptyState;

    private RecyclerView myListingsRecyclerView;
    private RecyclerView myWantsRecyclerView;

    private MyListingAdapter myListingAdapter;
    private MyWantAdapter myWantAdapter;

    private TextView profileSubtitle;
    private TextView profileInitials;
    private TextView profileName;
    private TextView profileEmail;
    private TextView profileRatingValue;
    private TextView profileRatingCount;
    private TextView profileListedCount;
    private TextView profileWantedCount;

    private List<Post> myListings;
    private List<Want> myWants;

    private Post postBeingEdited;

    private User user;

    private String name, surname, username, email, password, initials;
    private ArrayList<String> editableImages = new ArrayList<>();

    private final ActivityResultLauncher<String> editPostImagesLauncher =
            registerForActivityResult(new ActivityResultContracts.GetMultipleContents(), uris -> {
                if (uris != null && !uris.isEmpty()) {
                    for (Uri uri : uris) {
                        if (editableImages.size() < 10) {
                            editableImages.add(uri.toString());
                        }
                    }

                    if (editableImages.size() == 10) {
                        Toast.makeText(requireContext(), "Maximum 10 images reached", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileSubtitle = view.findViewById(R.id.profileSubtitle);
        profileInitials = view.findViewById(R.id.profileInitials);
        profileName = view.findViewById(R.id.profileName);
        profileEmail = view.findViewById(R.id.profileEmail);
        profileRatingValue = view.findViewById(R.id.profileRatingValue);
        profileRatingCount = view.findViewById(R.id.profileRatingCount);
        profileListedCount = view.findViewById(R.id.profileListedCount);
        profileWantedCount = view.findViewById(R.id.profileWantedCount);

        tabMyListings = view.findViewById(R.id.tabMyListings);
        tabMyWants = view.findViewById(R.id.tabMyWants);

        listingsSection = view.findViewById(R.id.listingsSection);
        wantsSection = view.findViewById(R.id.wantsSection);
        listingsEmptyState = view.findViewById(R.id.listingsEmptyState);
        wantsEmptyState = view.findViewById(R.id.wantsEmptyState);

        myListingsRecyclerView = view.findViewById(R.id.myListingsRecyclerView);
        myWantsRecyclerView = view.findViewById(R.id.myWantsRecyclerView);

        settingsButton = view.findViewById(R.id.settingsButton);

        user = SessionManager.getLoggedInUser(requireContext());
        name = user.getName();
        username = user.getUsername();
        surname = user.getSurname();
        email = user.getEmail();
        initials = String.valueOf(name.charAt(0)).toUpperCase() + String.valueOf(surname.charAt(0)).toUpperCase();


        profileSubtitle.setText("@"+username);
        profileInitials.setText(initials);
        profileName.setText(name + " " +surname);
        profileEmail.setText(email);
        profileRatingValue.setText("4.8");
        profileRatingCount.setText("12 ratings");

        loadProfileLists();

        showListingsTab();

        tabMyListings.setOnClickListener(v -> showListingsTab());
        tabMyWants.setOnClickListener(v -> showWantsTab());

        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), SettingsActivity.class);
            startActivity(intent);
        });
    }

    private void loadProfileLists() {
        myListings = new ArrayList<>();
        myWants = new ArrayList<>();

        myListingAdapter = new MyListingAdapter(myListings, this::showEditPostDialog);
        myListingsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        myListingsRecyclerView.setNestedScrollingEnabled(false);
        myListingsRecyclerView.setAdapter(myListingAdapter);

        myWantAdapter = new MyWantAdapter(requireContext(), myWants);
        myWantsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        myWantsRecyclerView.setNestedScrollingEnabled(false);
        myWantsRecyclerView.setAdapter(myWantAdapter);

        loadUserPosts();
        loadUserWants();
    }

    private void loadUserPosts() {
        UserRepository.getListings(user.getUserID(), new UserRepository.PostsCallback() {
            @Override
            public void onSuccess(List<Post> result) {
                if (!isAdded() || result == null) return;
                requireActivity().runOnUiThread(() -> {
                    myListings.clear();
                    myListings.addAll(result);
                    myListingAdapter.notifyDataSetChanged();

                    profileListedCount.setText(String.valueOf(myListings.size()));
                    listingsEmptyState.setVisibility(myListings.isEmpty() ? View.VISIBLE : View.GONE);
                    myListingsRecyclerView.setVisibility(myListings.isEmpty() ? View.GONE : View.VISIBLE);
                });
            }

            @Override
            public void onFailure(String message) {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), "Failed to load posts: " + message, Toast.LENGTH_SHORT).show()
                );
            }
        });
    }

    private void loadUserWants() {
        UserRepository.getWantRequests(user.getUserID(), new UserRepository.WantsCallback() {
            @Override
            public void onSuccess(List<Want> result) {
                if (!isAdded() || result == null) return;
                requireActivity().runOnUiThread(() -> {
                    myWants.clear();
                    myWants.addAll(result);
                    myWantAdapter.notifyDataSetChanged();

                    profileWantedCount.setText(String.valueOf(myWants.size()));
                    wantsEmptyState.setVisibility(myWants.isEmpty() ? View.VISIBLE : View.GONE);
                    myWantsRecyclerView.setVisibility(myWants.isEmpty() ? View.GONE : View.VISIBLE);
                });
            }

            @Override
            public void onFailure(String message) {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), "Failed to load wants: " + message, Toast.LENGTH_SHORT).show()
                );
            }
        });
    }


    private void showListingsTab() {
        listingsSection.setVisibility(View.VISIBLE);
        wantsSection.setVisibility(View.GONE);

        tabMyListings.setBackgroundTintList(ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.tab_active_bg)));
        tabMyListings.setTextColor(ContextCompat.getColor(requireContext(), R.color.tab_active_text));

        tabMyWants.setBackgroundTintList(ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.tab_inactive_bg)));
        tabMyWants.setTextColor(ContextCompat.getColor(requireContext(), R.color.tab_inactive_text));
    }

    private void showWantsTab() {
        listingsSection.setVisibility(View.GONE);
        wantsSection.setVisibility(View.VISIBLE);

        tabMyWants.setBackgroundTintList(ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.tab_active_bg)));
        tabMyWants.setTextColor(ContextCompat.getColor(requireContext(), R.color.tab_active_text));

        tabMyListings.setBackgroundTintList(ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.tab_inactive_bg)));
        tabMyListings.setTextColor(ContextCompat.getColor(requireContext(), R.color.tab_inactive_text));
    }

    private void showEditPostDialog(Post post) {
        postBeingEdited = post;
        editableImages.clear();
        if (post.getImageUris() != null) {
            editableImages.addAll(post.getImageUris());
        }

        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_post, null);

        TextInputEditText editName = dialogView.findViewById(R.id.editName);
        TextInputEditText editDescription = dialogView.findViewById(R.id.editDescription);
        TextInputEditText editPrice = dialogView.findViewById(R.id.editPrice);
        TextInputEditText editQuantity = dialogView.findViewById(R.id.editQuantity);
        TextInputEditText editLocation = dialogView.findViewById(R.id.editLocation);
        TextInputEditText editCondition = dialogView.findViewById(R.id.editCondition);
        MaterialButton saveBtn = dialogView.findViewById(R.id.saveBtn);
        MaterialButton addImagesBtn = dialogView.findViewById(R.id.addImagesBtn);
        MaterialButton removeImageBtn = dialogView.findViewById(R.id.removeImageBtn);
        MaterialButton deletePostBtn = dialogView.findViewById(R.id.deletePostBtn);

        editName.setText(post.getItemName());
        editDescription.setText(post.getItemDescription());
        editPrice.setText(String.valueOf(post.getPrice()));
        editQuantity.setText(String.valueOf(post.getQuantity()));
        editLocation.setText(post.getSellerLocation());
        editCondition.setText(post.getCondition());

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create();

        addImagesBtn.setOnClickListener(v -> editPostImagesLauncher.launch("image/*"));

        removeImageBtn.setOnClickListener(v -> {
            if (!editableImages.isEmpty()) {
                editableImages.remove(editableImages.size() - 1);
            } else {
                Toast.makeText(requireContext(), "No images to remove", Toast.LENGTH_SHORT).show();
            }
        });

        saveBtn.setOnClickListener(v -> {
            String name = getText(editName);
            String description = getText(editDescription);
            String priceText = getText(editPrice);
            String quantityText = getText(editQuantity);
            String location = getText(editLocation);
            String condition = getText(editCondition);

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(description) ||
                    TextUtils.isEmpty(priceText) || TextUtils.isEmpty(quantityText) ||
                    TextUtils.isEmpty(location) || TextUtils.isEmpty(condition)) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                post.setItemName(name);
                post.setItemDescription(description);
                post.setPrice(Double.parseDouble(priceText));
                post.setQuantity(Integer.parseInt(quantityText));
                post.setSellerLocation(location);
                post.setCondition(condition);
                post.setImageUris(new ArrayList<>(editableImages));

                PostRepository.updateItem(post, new PostRepository.ActionCallback() {
                    @Override
                    public void onSuccess(String message) {
                        if (!isAdded()) return;
                        requireActivity().runOnUiThread(() -> {
                            loadProfileLists();
                            Toast.makeText(requireContext(), "Item updated successfully", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        });
                    }

                    @Override
                    public void onFailure(String message) {
                        if (!isAdded()) return;
                        requireActivity().runOnUiThread(() ->
                                Toast.makeText(requireContext(), "Update failed: " + message, Toast.LENGTH_SHORT).show()
                        );
                    }
                });

            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), "Enter valid price and quantity", Toast.LENGTH_SHORT).show();
            }
        });

        deletePostBtn.setOnClickListener(v -> {
            PostRepository.deleteItem(post.getItemID(), new PostRepository.ActionCallback() {
                @Override
                public void onSuccess(String message) {
                    if (!isAdded()) return;
                    requireActivity().runOnUiThread(() -> {
                        loadProfileLists();
                        Toast.makeText(requireContext(), "Post deleted", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    });
                }

                @Override
                public void onFailure(String message) {
                    if (!isAdded()) return;
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(requireContext(), "Delete failed: " + message, Toast.LENGTH_SHORT).show()
                    );
                }
            });
        });

        dialog.show();
    }

    private String getText(TextInputEditText input) {
        return input.getText() == null ? "" : input.getText().toString().trim();
    }
}