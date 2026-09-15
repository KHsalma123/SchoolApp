package com.schoolapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.schoolapp.R;
import com.schoolapp.activities.MessageActivity;
import com.schoolapp.adapters.MessagesAdapter;
import com.schoolapp.database.DatabaseHelper;
import com.schoolapp.models.Message;
import com.schoolapp.utils.PrefsManager;
import java.util.List;

public class MessagesFragment extends Fragment {

    private RecyclerView rvMessages;
    private TextView tvEmpty;
    private FloatingActionButton fabNewMessage;
    private ProgressBar pbLoading;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_messages, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvMessages    = view.findViewById(R.id.rv_messages);
        tvEmpty       = view.findViewById(R.id.tv_empty);
        fabNewMessage = view.findViewById(R.id.fab_new_message);
        pbLoading     = view.findViewById(R.id.pb_loading);

        rvMessages.setLayoutManager(new LinearLayoutManager(getContext()));

        fabNewMessage.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MessageActivity.class);
            startActivity(intent);
        });

        loadMessages();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadMessages();
    }

    private void loadMessages() {
        pbLoading.setVisibility(View.VISIBLE);

        new Thread(() -> {
            int userId = PrefsManager.getInstance(requireContext()).getUserId();
            List<Message> messages = DatabaseHelper.getInstance(requireContext())
                    .getMessagesForUser(userId);

            new Handler(Looper.getMainLooper()).post(() -> {
                if (!isAdded()) return;
                pbLoading.setVisibility(View.GONE);
                if (messages.isEmpty()) {
                    tvEmpty.setVisibility(View.VISIBLE);
                    rvMessages.setVisibility(View.GONE);
                } else {
                    tvEmpty.setVisibility(View.GONE);
                    rvMessages.setVisibility(View.VISIBLE);
                    rvMessages.setAdapter(new MessagesAdapter(messages, msg -> {
                        Intent intent = new Intent(getActivity(), MessageActivity.class);
                        intent.putExtra("sender_name", msg.getSenderName());
                        intent.putExtra("message_id", msg.getId());
                        startActivity(intent);
                    }));
                }
            });
        }).start();
    }
}
