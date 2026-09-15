package com.schoolapp.models;

public class Message {
    private int id;
    private int senderId;
    private int receiverId;
    private String senderName;
    private String receiverName;
    private String content;
    private String timestamp;
    private boolean isRead;
    private boolean isSentByMe;
    private String senderAvatar;
    private String subject;

    public Message() {}

    public Message(int id, String senderName, String content, String timestamp,
                   boolean isRead, boolean isSentByMe) {
        this.id = id;
        this.senderName = senderName;
        this.content = content;
        this.timestamp = timestamp;
        this.isRead = isRead;
        this.isSentByMe = isSentByMe;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getSenderId() { return senderId; }
    public void setSenderId(int senderId) { this.senderId = senderId; }
    public int getReceiverId() { return receiverId; }
    public void setReceiverId(int receiverId) { this.receiverId = receiverId; }
    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }
    public String getReceiverName() { return receiverName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
    public boolean isSentByMe() { return isSentByMe; }
    public void setSentByMe(boolean sentByMe) { isSentByMe = sentByMe; }
    public String getSenderAvatar() { return senderAvatar; }
    public void setSenderAvatar(String senderAvatar) { this.senderAvatar = senderAvatar; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getInitials() {
        if (senderName != null && senderName.length() >= 2) {
            String[] parts = senderName.split(" ");
            if (parts.length >= 2)
                return String.valueOf(parts[0].charAt(0)) + String.valueOf(parts[1].charAt(0));
            return senderName.substring(0, 2).toUpperCase();
        }
        return "?";
    }
}
