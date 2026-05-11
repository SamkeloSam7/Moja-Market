package com.example.mojamarket;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatPreviewAdapter extends RecyclerView.Adapter<ChatPreviewAdapter.ChatPreviewViewHolder> {

    private final Context context;
    private final List<ChatPreview> chatList;

    public ChatPreviewAdapter(Context context, List<ChatPreview> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ChatPreviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat_preview, parent, false);
        return new ChatPreviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatPreviewViewHolder holder, int position) {
        ChatPreview chat = chatList.get(position);

        holder.chatName.setText(chat.getName());
        holder.chatLastMessage.setText(chat.getLastMessage());
        holder.chatTime.setText(chat.getTime());

        String firstLetter = chat.getName().substring(0, 1).toUpperCase();
        holder.chatAvatarLetter.setText(firstLetter);

        if (chat.getUnread() > 0) {
            holder.chatUnreadBadge.setVisibility(View.VISIBLE);
            holder.chatUnreadBadge.setText(String.valueOf(chat.getUnread()));
            holder.chatLastMessage.setTextColor(ContextCompat.getColor(context, R.color.primary_text));
            holder.chatLastMessage.setTypeface(null, Typeface.BOLD);
        } else {
            holder.chatUnreadBadge.setVisibility(View.GONE);
            holder.chatLastMessage.setTextColor(ContextCompat.getColor(context, R.color.secondary_text));
            holder.chatLastMessage.setTypeface(null, Typeface.NORMAL);
        }

        // Pass the correct extras that ChatActivity expects
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("chatID", String.valueOf(chat.getUserId()));
            intent.putExtra("currentUserID", chat.getUserId());
            intent.putExtra("name", chat.getName());
            intent.putExtra("username", chat.getUsername());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public static class ChatPreviewViewHolder extends RecyclerView.ViewHolder {
        TextView chatAvatarLetter, chatUnreadBadge, chatName, chatLastMessage, chatTime;

        public ChatPreviewViewHolder(@NonNull View itemView) {
            super(itemView);
            chatAvatarLetter = itemView.findViewById(R.id.chatAvatarLetter);
            chatUnreadBadge = itemView.findViewById(R.id.chatUnreadBadge);
            chatName = itemView.findViewById(R.id.chatName);
            chatLastMessage = itemView.findViewById(R.id.chatLastMessage);
            chatTime = itemView.findViewById(R.id.chatTime);
        }
    }
}