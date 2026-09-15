package com.schoolapp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.schoolapp.R;
import com.schoolapp.adapters.ChatAdapter;
import com.schoolapp.database.DatabaseHelper;
import com.schoolapp.models.*;
import com.schoolapp.utils.NotificationHelper;
import com.schoolapp.utils.PrefsManager;
import java.text.SimpleDateFormat;
import java.util.*;

public class MessageActivity extends AppCompatActivity {

    private static final int REQUEST_SMS_PERMISSION = 101;
    private static final int REQUEST_CALL_PERMISSION = 102;

    private static final long AUTO_REPLY_DELAY_MS = 1500;
    private static final String AUTO_REPLY_TEXT =
            "Merci pour votre message, je reviens vers vous rapidement.";

    private RecyclerView rvChat;
    private TextInputEditText etMessage;
    private MaterialButton btnSend;
    private ImageButton btnSms, btnCall;
    private TextView tvTitle, tvSubtitle;
    private ImageButton btnBack;

    private String teacherName = "";
    private String teacherPhone = "0612345678";
    private List<Message> messages = new ArrayList<>();
    private ChatAdapter adapter;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        rvChat     = findViewById(R.id.rv_chat);
        etMessage  = findViewById(R.id.et_message);
        btnSend    = findViewById(R.id.btn_send);
        btnSms     = findViewById(R.id.btn_sms);
        btnCall    = findViewById(R.id.btn_call);
        tvTitle    = findViewById(R.id.tv_title);
        tvSubtitle = findViewById(R.id.tv_subtitle);
        btnBack    = findViewById(R.id.btn_back);

        teacherName = getIntent().getStringExtra("sender_name");

        PrefsManager prefs = PrefsManager.getInstance(this);
        if (!prefs.isParent()) {
            btnSms.setVisibility(View.GONE);
            btnCall.setVisibility(View.GONE);
        }

        if (teacherName == null || teacherName.isEmpty()) teacherName = "Nouvel enseignant";

        tvTitle.setText(teacherName);
        tvSubtitle.setText("Professeur");

        btnBack.setOnClickListener(v -> finish());

        // ← LinearLayoutManager normal (pas de reverseLayout)
        rvChat.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatAdapter(messages);
        rvChat.setAdapter(adapter);

        loadMessages();

        btnSend.setOnClickListener(v -> sendInAppMessage());
        btnSms.setOnClickListener(v -> sendSmsIntent());
        btnCall.setOnClickListener(v -> makeCall());
    }

    private void loadMessages() {
        int currentUserId = PrefsManager.getInstance(this).getUserId();
        List<Message> loaded = DatabaseHelper.getInstance(this).getMessagesForUser(currentUserId);
        messages.clear();
        for (Message m : loaded) {
            m.setSentByMe(m.getSenderId() == currentUserId);
            if (teacherName.equals(m.getSenderName()) || teacherName.equals(m.getReceiverName())) {
                messages.add(m);
            }
        }
        // ✅ SUPPRIMÉ : Collections.reverse(messages) ← c'était le problème
        adapter.notifyDataSetChanged();
        if (!messages.isEmpty()) rvChat.scrollToPosition(messages.size() - 1);
    }

    private void sendInAppMessage() {
        String content = etMessage.getText() != null ? etMessage.getText().toString().trim() : "";
        if (TextUtils.isEmpty(content)) return;

        Message msg = new Message();
        msg.setSenderId(PrefsManager.getInstance(this).getUserId());
        msg.setReceiverId(99);
        msg.setSenderName(PrefsManager.getInstance(this).getUserName());
        msg.setReceiverName(teacherName);
        msg.setContent(content);
        msg.setTimestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.FRENCH).format(new Date()));
        msg.setSentByMe(true);

        DatabaseHelper.getInstance(this).insertMessage(msg);

        messages.add(msg); // ✅ ajout à la fin = ordre correct
        adapter.notifyItemInserted(messages.size() - 1);
        rvChat.scrollToPosition(messages.size() - 1);
        etMessage.setText("");

        handler.postDelayed(this::receiveAutoReply, AUTO_REPLY_DELAY_MS);
    }

    private void receiveAutoReply() {
        Message reply = new Message();
        reply.setSenderId(99);
        reply.setReceiverId(PrefsManager.getInstance(this).getUserId());
        reply.setSenderName(teacherName);
        reply.setReceiverName(PrefsManager.getInstance(this).getUserName());
        reply.setContent(AUTO_REPLY_TEXT);
        reply.setTimestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.FRENCH).format(new Date()));
        reply.setSentByMe(false);

        DatabaseHelper.getInstance(this).insertMessage(reply);

        messages.add(reply); // ajout à la fin = ordre correct
        adapter.notifyItemInserted(messages.size() - 1);
        rvChat.scrollToPosition(messages.size() - 1);

        NotificationHelper.showMessageNotification(this, teacherName, AUTO_REPLY_TEXT);
    }

    private void sendSmsIntent() {
        String text = etMessage.getText() != null ?
                etMessage.getText().toString().trim() : "";

        Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
        smsIntent.setData(Uri.parse("smsto:" + teacherPhone));
        smsIntent.putExtra("sms_body", text);
        startActivity(smsIntent);
    }

    private void makeCall() {
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + teacherPhone));
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            startActivity(callIntent);
        } else {
            Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + teacherPhone));
            startActivity(dialIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] perms, int[] results) {
        super.onRequestPermissionsResult(requestCode, perms, results);
        if (requestCode == REQUEST_SMS_PERMISSION &&
                results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED) {
            sendSmsIntent();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}