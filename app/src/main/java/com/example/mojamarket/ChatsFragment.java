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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.example.mojamarket.network.ApiClient;
import com.example.mojamarket.session.SessionManager;
import com.example.mojamarket.network.ApiConstants;

public class ChatsFragment extends Fragment {

    private RecyclerView chatsRecyclerView;
    private LinearLayout emptyStateLayout;
    private TextView messagesSubtitle;
    private ChatPreviewAdapter chatPreviewAdapter;
    private List<ChatPreview> chatList;

    private String BASE_URL;

    public ChatsFragment() {}

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
        BASE_URL = ApiConstants.BASE_URL;

        // Initialize the adapter before the API call so it is ready when data arrives
        chatPreviewAdapter = new ChatPreviewAdapter(requireContext(), chatList);
        chatsRecyclerView.setAdapter(chatPreviewAdapter);

        String userID = SessionManager.getLoggedInUser(requireContext()).getUserID();
        JSONObject body = new JSONObject();
        try {
            body.put("userID", userID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ApiClient.getInstance().post(BASE_URL + "/api/chat/list", body, new ApiClient.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                requireActivity().runOnUiThread(() -> {
                    chatList.clear();
                    JSONArray data = response.optJSONArray("data");
                    if (data != null) {
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject c = data.optJSONObject(i);
                            if (c == null) continue;
                            chatList.add(new ChatPreview(
                                    c.optInt("chat_id"),  // chat_id is an integer, not a string
                                    c.optString("name"),
                                    c.optString("username"),
                                    c.optString("last_message", ""),
                                    c.optString("last_message_time", ""),
                                    0
                            ));
                        }
                    }

                    // Count unread chats and update the subtitle after data has loaded
                    int unreadCount = 0;
                    for (ChatPreview chat : chatList) {
                        if (chat.getUnread() > 0) unreadCount++;
                    }
                    messagesSubtitle.setText(unreadCount + " unread");

                    // Show or hide the empty state depending on whether any chats were returned
                    if (chatList.isEmpty()) {
                        emptyStateLayout.setVisibility(View.VISIBLE);
                        chatsRecyclerView.setVisibility(View.GONE);
                    } else {
                        emptyStateLayout.setVisibility(View.GONE);
                        chatsRecyclerView.setVisibility(View.VISIBLE);
                    }

                    chatPreviewAdapter.notifyDataSetChanged();
                });
            }

            @Override
            public void onFailure(String e) {}
        });
    }
}