package com.example.mojamarket;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mojamarket.models.Post;
import com.example.mojamarket.session.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class PostFragment extends Fragment {

    private ArrayList<Uri> selectedImages = new ArrayList<>();

    private LinearLayout uploadBox;
    private TextInputEditText itemNameInput;
    private TextInputEditText itemDescriptionInput;
    private TextInputEditText itemPriceInput;
    private TextInputEditText itemStockInput;
    private TextInputEditText itemLocationInput;
    private Spinner itemConditionSpinner;
    private MaterialButton postItemButton;
    private ImageView uploadPreviewImage;
    private ImageView uploadIcon;
    private TextView uploadText;
    private TextView uploadSubtext;

    private final ActivityResultLauncher<String> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetMultipleContents(), uris -> {
                if (uris != null && !uris.isEmpty()) {

                    if (uris.size() > 10) {
                        Toast.makeText(requireContext(), "Maximum 10 images allowed", Toast.LENGTH_SHORT).show();
                        selectedImages = new ArrayList<>(uris.subList(0, 10));
                    } else {
                        selectedImages = new ArrayList<>(uris);
                    }

                    uploadPreviewImage.setImageURI(selectedImages.get(0));
                    uploadPreviewImage.setVisibility(View.VISIBLE);
                    uploadIcon.setVisibility(View.GONE);

                    uploadText.setText(selectedImages.size() + " images selected");
                    uploadSubtext.setText("Tap to change images");
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

        uploadBox.setOnClickListener(v -> imagePickerLauncher.launch("image/*"));

        postItemButton.setOnClickListener(v -> {
            String name = getText(itemNameInput);
            String description = getText(itemDescriptionInput);
            String price = getText(itemPriceInput);
            String stock = getText(itemStockInput);
            String location = getText(itemLocationInput);

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(description) ||
                    TextUtils.isEmpty(price) || TextUtils.isEmpty(stock) || TextUtils.isEmpty(location)) {
                Toast.makeText(requireContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedImages.size() < 1) {
                Toast.makeText(requireContext(), "Upload at least 1 image", Toast.LENGTH_SHORT).show();
                return;
            }

            double priceValue = Double.parseDouble(price);
            int stockValue = Integer.parseInt(stock);
            String condition = itemConditionSpinner.getSelectedItem().toString();

            Post post = new Post(
                    name,
                    description,
                    condition,
                    priceValue,
                    stockValue,
                    stockValue > 0 ? "Available" : "Sold Out",
                    location,
                    requireContext()
            );

            ArrayList<String> imagePaths = new ArrayList<>();
            for (Uri uri : selectedImages) {
                imagePaths.add(uri.toString());
            }
            post.setImageUris(imagePaths);

            SessionManager.getLoggedInUser(requireContext()).postItem(requireContext(), post);

            Toast.makeText(requireContext(), "Item posted!", Toast.LENGTH_SHORT).show();

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
        });
    }

    private String getText(TextInputEditText input) {
        return input.getText() == null ? "" : input.getText().toString().trim();
    }
}