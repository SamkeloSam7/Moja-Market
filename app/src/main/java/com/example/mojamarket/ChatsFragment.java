package com.example.mojamarket;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {

    private RecyclerView chatsRecyclerView;
    private LinearLayout emptyStateLayout;
    private TextView messagesSubtitle;
    private ChatPreviewAdapter chatPreviewAdapter;
    private List<ChatPreview> chatList;

    public ChatsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chatsRecyclerView = view.findViewById(R.id.chatsRecyclerView);
        emptyStateLayout = view.findViewById(R.id.emptyStateLayout);
        messagesSubtitle = view.findViewById(R.id.messagesSubtitle);

        chatsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        chatList = new ArrayList<>();

        chatList.add(new ChatPreview(
                1,
                "Samkelo Mthembu",
                "samkelo",
                "Hey, is this item still available?",
                "2:45 PM",
                2
        ));

        chatList.add(new ChatPreview(
                2,
                "Yamkela Dlamini",
                "yamkela_j",
                "Yes, I can meet tomorrow.",
                "Yesterday",
                0
        ));

        chatList.add(new ChatPreview(
                3,
                "Blessings Nkosi",
                "blessings_k",
                "Thanks, I’m interested.",
                "3 days ago",
                1
        ));

        int unreadCount = 0;
        for (ChatPreview chat : chatList) {
            if (chat.getUnread() > 0) {
                unreadCount++;
            }
        }

        messagesSubtitle.setText(unreadCount + " unread");

        if (chatList.isEmpty()) {
            emptyStateLayout.setVisibility(View.VISIBLE);
            chatsRecyclerView.setVisibility(View.GONE);
        } else {
            emptyStateLayout.setVisibility(View.GONE);
            chatsRecyclerView.setVisibility(View.VISIBLE);
            chatPreviewAdapter = new ChatPreviewAdapter(requireContext(), chatList);
            chatsRecyclerView.setAdapter(chatPreviewAdapter);
        }
    }
}