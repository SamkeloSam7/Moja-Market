package com.example.mojamarket;

import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.mojamarket.models.Post;
import com.example.mojamarket.models.Want;
import com.example.mojamarket.network.PostRepository;
import com.example.mojamarket.network.WantRepository;
import com.example.mojamarket.session.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class PostFragment extends Fragment {

    private ArrayList<Uri> selectedImages = new ArrayList<>();

    private MaterialButton tabSellItem, tabWantRequest;
    private LinearLayout itemFormLayout, wantFormLayout, uploadBox;
    private TextInputEditText itemNameInput, itemDescriptionInput, itemPriceInput,
            itemStockInput, itemLocationInput;
    private Spinner itemConditionSpinner;
    private MaterialButton postItemButton;
    private TextInputEditText wantItemNameInput, wantDescriptionInput, wantBudgetInput;
    private MaterialButton postWantButton;
    private ImageView uploadPreviewImage, uploadIcon;
    private TextView uploadText, uploadSubtext;

    private final ActivityResultLauncher<String> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetMultipleContents(), uris -> {
                if (uris != null && !uris.isEmpty()) {
                    selectedImages = uris.size() > 10
                            ? new ArrayList<>(uris.subList(0, 10))
                            : new ArrayList<>(uris);

                    if (uris.size() > 10)
                        Toast.makeText(requireContext(), "Maximum 10 images allowed", Toast.LENGTH_SHORT).show();

                    uploadPreviewImage.setImageURI(selectedImages.get(0));
                    uploadPreviewImage.setVisibility(View.VISIBLE);
                    uploadIcon.setVisibility(View.GONE);
                    uploadText.setText(selectedImages.size() + " images selected");
                    uploadSubtext.setText("Tap to change images");
                }
            });

    public PostFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabSellItem = view.findViewById(R.id.tabSellItem);
        tabWantRequest = view.findViewById(R.id.tabWantRequest);
        itemFormLayout = view.findViewById(R.id.itemFormLayout);
        wantFormLayout = view.findViewById(R.id.wantFormLayout);
        uploadBox = view.findViewById(R.id.uploadBox);
        uploadPreviewImage = view.findViewById(R.id.uploadPreviewImage);
        uploadIcon = view.findViewById(R.id.uploadIcon);
        uploadText = view.findViewById(R.id.uploadText);
        uploadSubtext = view.findViewById(R.id.uploadSubtext);
        itemNameInput = view.findViewById(R.id.itemNameInput);
        itemDescriptionInput = view.findViewById(R.id.itemDescriptionInput);
        itemPriceInput = view.findViewById(R.id.itemPriceInput);
        itemStockInput = view.findViewById(R.id.itemStockInput);
        itemLocationInput = view.findViewById(R.id.itemLocationInput);
        itemConditionSpinner = view.findViewById(R.id.itemConditionSpinner);
        postItemButton = view.findViewById(R.id.postItemButton);
        wantItemNameInput = view.findViewById(R.id.wantItemNameInput);
        wantDescriptionInput = view.findViewById(R.id.wantDescriptionInput);
        wantBudgetInput = view.findViewById(R.id.wantBudgetInput);
        postWantButton = view.findViewById(R.id.postWantButton);

        String[] conditions = {"New", "Like New", "Good", "Used"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, conditions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemConditionSpinner.setAdapter(adapter);

        showItemForm();
        tabSellItem.setOnClickListener(v -> showItemForm());
        tabWantRequest.setOnClickListener(v -> showWantForm());
        uploadBox.setOnClickListener(v -> imagePickerLauncher.launch("image/*"));

        postItemButton.setOnClickListener(v -> handlePostItem());
        postWantButton.setOnClickListener(v -> handlePostWant());
    }

    private void handlePostItem() {
        String name = getText(itemNameInput);
        String description = getText(itemDescriptionInput);
        String price = getText(itemPriceInput);
        String stock = getText(itemStockInput);
        String location = getText(itemLocationInput);

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(description) ||
                TextUtils.isEmpty(price) || TextUtils.isEmpty(stock) || TextUtils.isEmpty(location)) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double priceValue = Double.parseDouble(price);
            int stockValue = Integer.parseInt(stock);
            String condition = itemConditionSpinner.getSelectedItem().toString();

            Post post = new Post(
                    name, description, condition, priceValue, stockValue,
                    stockValue > 0 ? "Available" : "Sold Out",
                    location, requireContext()
            );

            ArrayList<String> imagePaths = new ArrayList<>();
            for (Uri uri : selectedImages) imagePaths.add(uri.toString());
            post.setImageUris(imagePaths);

            postItemButton.setEnabled(false);

            PostRepository.postItem(post, new PostRepository.ActionCallback() {
                @Override
                public void onSuccess(String message) {
                    if (!isAdded()) return;
                    requireActivity().runOnUiThread(() -> {
                        postItemButton.setEnabled(true);
                        Toast.makeText(requireContext(), "Item posted successfully!", Toast.LENGTH_SHORT).show();
                        resetItemForm();
                    });
                }

                @Override
                public void onFailure(String message) {
                    if (!isAdded()) return;
                    requireActivity().runOnUiThread(() -> {
                        postItemButton.setEnabled(true);
                        Toast.makeText(requireContext(), "Failed to post item: " + message, Toast.LENGTH_SHORT).show();
                    });
                }
            });
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Invalid number format", Toast.LENGTH_SHORT).show();
        }
    }

    private void handlePostWant() {
        String itemName = getText(wantItemNameInput);
        String description = getText(wantDescriptionInput);
        String budgetStr = getText(wantBudgetInput);

        if (TextUtils.isEmpty(itemName) || TextUtils.isEmpty(description) || TextUtils.isEmpty(budgetStr)) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double budget = Double.parseDouble(budgetStr);
            Want want = new Want(itemName, description, budget, SessionManager.getLoggedInUser(requireContext()));

            postWantButton.setEnabled(false);

            WantRepository.postWant(want, new WantRepository.ActionCallback() {
                @Override
                public void onSuccess(String message) {
                    if (!isAdded()) return;
                    requireActivity().runOnUiThread(() -> {
                        postWantButton.setEnabled(true);
                        Toast.makeText(requireContext(), "Want request posted successfully!", Toast.LENGTH_SHORT).show();
                        wantItemNameInput.setText("");
                        wantDescriptionInput.setText("");
                        wantBudgetInput.setText("");
                    });
                }

                @Override
                public void onFailure(String message) {
                    if (!isAdded()) return;
                    requireActivity().runOnUiThread(() -> {
                        postWantButton.setEnabled(true);
                        Toast.makeText(requireContext(), "Failed to post want: " + message, Toast.LENGTH_SHORT).show();
                    });
                }
            });
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Invalid number format", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetItemForm() {
        selectedImages.clear();
        uploadPreviewImage.setVisibility(View.GONE);
        uploadIcon.setVisibility(View.VISIBLE);
        uploadText.setText("Upload Images");
        uploadSubtext.setText("1–10 images");
        itemNameInput.setText("");
        itemDescriptionInput.setText("");
        itemPriceInput.setText("");
        itemStockInput.setText("");
        itemLocationInput.setText("");
        itemConditionSpinner.setSelection(0);
    }

    private void showItemForm() {
        itemFormLayout.setVisibility(View.VISIBLE);
        wantFormLayout.setVisibility(View.GONE);
        tabSellItem.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.tab_active_bg)));
        tabSellItem.setTextColor(ContextCompat.getColor(requireContext(), R.color.tab_active_text));
        tabWantRequest.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.tab_inactive_bg)));
        tabWantRequest.setTextColor(ContextCompat.getColor(requireContext(), R.color.tab_inactive_text));
    }

    private void showWantForm() {
        itemFormLayout.setVisibility(View.GONE);
        wantFormLayout.setVisibility(View.VISIBLE);
        tabWantRequest.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.tab_active_bg)));
        tabWantRequest.setTextColor(ContextCompat.getColor(requireContext(), R.color.tab_active_text));
        tabSellItem.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.tab_inactive_bg)));
        tabSellItem.setTextColor(ContextCompat.getColor(requireContext(), R.color.tab_inactive_text));
    }

    private String getText(TextInputEditText input) {
        return input.getText() == null ? "" : input.getText().toString().trim();
    }
}