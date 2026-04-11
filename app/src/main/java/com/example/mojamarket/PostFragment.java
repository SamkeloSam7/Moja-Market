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

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class PostFragment extends Fragment {

    private MaterialButton tabSellItem;
    private MaterialButton tabWantRequest;

    private LinearLayout itemFormLayout;
    private LinearLayout wantFormLayout;
    private LinearLayout uploadBox;

    private TextInputEditText itemNameInput;
    private TextInputEditText itemDescriptionInput;
    private TextInputEditText itemPriceInput;
    private TextInputEditText itemStockInput;
    private TextInputEditText itemLocationInput;
    private Spinner itemConditionSpinner;
    private MaterialButton postItemButton;

    private TextInputEditText wantItemNameInput;
    private TextInputEditText wantDescriptionInput;
    private TextInputEditText wantBudgetInput;
    private MaterialButton postWantButton;

    private ImageView uploadPreviewImage;
    private ImageView uploadIcon;
    private TextView uploadText;
    private TextView uploadSubtext;

    private Uri selectedImageUri;

    private final ActivityResultLauncher<String> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    uploadPreviewImage.setImageURI(uri);
                    uploadPreviewImage.setVisibility(View.VISIBLE);
                    uploadIcon.setVisibility(View.GONE);
                    uploadText.setText("Image selected");
                    uploadSubtext.setText("Tap again to change image");
                }
            });

    public PostFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

        uploadPreviewImage = view.findViewById(R.id.uploadPreviewImage);
        uploadIcon = view.findViewById(R.id.uploadIcon);
        uploadText = view.findViewById(R.id.uploadText);
        uploadSubtext = view.findViewById(R.id.uploadSubtext);

        String[] conditions = {"New", "Like New", "Good", "Used"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                conditions
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemConditionSpinner.setAdapter(adapter);

        showItemForm();

        tabSellItem.setOnClickListener(v -> showItemForm());
        tabWantRequest.setOnClickListener(v -> showWantForm());

        uploadBox.setOnClickListener(v -> imagePickerLauncher.launch("image/*"));

        postItemButton.setOnClickListener(v -> {
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

            if (selectedImageUri == null) {
                Toast.makeText(requireContext(), "Please upload an image", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(requireContext(), "Item posted successfully!", Toast.LENGTH_SHORT).show();
        });

        postWantButton.setOnClickListener(v -> {
            String itemName = getText(wantItemNameInput);
            String description = getText(wantDescriptionInput);
            String budget = getText(wantBudgetInput);

            if (TextUtils.isEmpty(itemName) || TextUtils.isEmpty(description) || TextUtils.isEmpty(budget)) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(requireContext(), "Want request posted successfully!", Toast.LENGTH_SHORT).show();
        });
    }

    private void showItemForm() {
        itemFormLayout.setVisibility(View.VISIBLE);
        wantFormLayout.setVisibility(View.GONE);

        tabSellItem.setBackgroundTintList(ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.tab_active_bg)));
        tabSellItem.setTextColor(ContextCompat.getColor(requireContext(), R.color.tab_active_text));

        tabWantRequest.setBackgroundTintList(ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.tab_inactive_bg)));
        tabWantRequest.setTextColor(ContextCompat.getColor(requireContext(), R.color.tab_inactive_text));
    }

    private void showWantForm() {
        itemFormLayout.setVisibility(View.GONE);
        wantFormLayout.setVisibility(View.VISIBLE);

        tabWantRequest.setBackgroundTintList(ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.tab_active_bg)));
        tabWantRequest.setTextColor(ContextCompat.getColor(requireContext(), R.color.tab_active_text));

        tabSellItem.setBackgroundTintList(ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.tab_inactive_bg)));
        tabSellItem.setTextColor(ContextCompat.getColor(requireContext(), R.color.tab_inactive_text));
    }

    private String getText(TextInputEditText input) {
        return input.getText() == null ? "" : input.getText().toString().trim();
    }
}