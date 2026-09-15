package com.schoolapp.models;

public class Document {
    public static final String TYPE_BULLETIN = "BULLETIN";
    public static final String TYPE_CERTIFICATE = "CERTIFICATE";
    public static final String TYPE_OTHER = "OTHER";

    private int id;
    private int studentId;
    private String name;
    private String type;
    private String filePath;
    private String date;
    private long fileSize;

    public Document() {}

    public Document(int id, int studentId, String name, String type, String filePath, String date) {
        this.id = id;
        this.studentId = studentId;
        this.name = name;
        this.type = type;
        this.filePath = filePath;
        this.date = date;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public long getFileSize() { return fileSize; }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }

    public String getFormattedSize() {
        if (fileSize < 1024) return fileSize + " B";
        else if (fileSize < 1024 * 1024) return (fileSize / 1024) + " KB";
        else return String.format("%.1f MB", fileSize / (1024.0 * 1024.0));
    }
}
