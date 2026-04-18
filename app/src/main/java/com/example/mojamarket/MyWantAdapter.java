package com.example.mojamarket;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mojamarket.models.Want;
import com.example.mojamarket.utility.WantDatabase;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class MyWantAdapter extends RecyclerView.Adapter<MyWantAdapter.MyWantViewHolder> {

    private final Context context;
    private final List<Want> wantList;

    public MyWantAdapter(Context context, List<Want> wantList) {
        this.context = context;
        this.wantList = wantList;
    }

    @NonNull
    @Override
    public MyWantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile_want, parent, false);
        return new MyWantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyWantViewHolder holder, int position) {
        Want want = wantList.get(position);

        holder.profileWantName.setText(want.getItem());
        holder.profileWantDescription.setText(want.getDescription());

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        holder.profileWantBudget.setText("Budget: R" + numberFormat.format(want.getBudget()));

        if (want.isWantStatus()) {
            holder.profileWantStatus.setText("Looking");
            holder.profileWantStatus.setBackgroundResource(R.drawable.bg_looking_badge);
            holder.profileWantStatus.setTextColor(0xFFFFFFFF);
        } else {
            holder.profileWantStatus.setText("Fulfilled");
            holder.profileWantStatus.setBackgroundResource(R.drawable.bg_fulfilled_badge);
            holder.profileWantStatus.setTextColor(0xFF6B7280);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, WantDetailActivity.class);
            intent.putExtra("want_name", want.getItem());
            context.startActivity(intent);
        });

        holder.editWantBtn.setOnClickListener(v -> showEditWantDialog(want));
    }

    @Override
    public int getItemCount() {
        return wantList.size();
    }

    private void showEditWantDialog(Want want) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_want, null);

        TextInputEditText editName = dialogView.findViewById(R.id.editName);
        TextInputEditText editDescription = dialogView.findViewById(R.id.editDescription);
        TextInputEditText editBudget = dialogView.findViewById(R.id.editBudget);
        MaterialButton saveBtn = dialogView.findViewById(R.id.saveBtn);
        MaterialButton deleteWantBtn = dialogView.findViewById(R.id.deleteWantBtn);

        editName.setText(want.getItem());
        editDescription.setText(want.getDescription());
        editBudget.setText(String.valueOf(want.getBudget()));

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .create();

        saveBtn.setOnClickListener(v -> {
            String name = getText(editName);
            String description = getText(editDescription);
            String budgetText = getText(editBudget);

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(description) || TextUtils.isEmpty(budgetText)) {
                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                want.setItem(name);
                want.setDescription(description);
                want.setBudget(Double.parseDouble(budgetText));

                WantDatabase.updateWant(context, want);
                notifyDataSetChanged();
                Toast.makeText(context, "Want request updated successfully", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } catch (NumberFormatException e) {
                Toast.makeText(context, "Enter a valid budget", Toast.LENGTH_SHORT).show();
            }
        });

        deleteWantBtn.setOnClickListener(v -> {
            WantDatabase.deleteWant(context, want.getItem());
            wantList.remove(want);
            notifyDataSetChanged();
            Toast.makeText(context, "Want deleted", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }

    private String getText(TextInputEditText input) {
        return input.getText() == null ? "" : input.getText().toString().trim();
    }

    static class MyWantViewHolder extends RecyclerView.ViewHolder {
        TextView profileWantName, profileWantStatus, profileWantDescription, profileWantBudget;
        ImageButton editWantBtn;

        public MyWantViewHolder(@NonNull View itemView) {
            super(itemView);
            profileWantName = itemView.findViewById(R.id.profileWantName);
            profileWantStatus = itemView.findViewById(R.id.profileWantStatus);
            profileWantDescription = itemView.findViewById(R.id.profileWantDescription);
            profileWantBudget = itemView.findViewById(R.id.profileWantBudget);
            editWantBtn = itemView.findViewById(R.id.editWantBtn);
        }
    }
}