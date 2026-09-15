package com.schoolapp.models;

public class Absence {
    public static final String TYPE_ABSENCE = "ABSENCE";
    public static final String TYPE_LATE = "LATE";
    public static final String STATUS_JUSTIFIED = "JUSTIFIED";
    public static final String STATUS_UNJUSTIFIED = "UNJUSTIFIED";
    public static final String STATUS_PENDING = "PENDING";

    private int id;
    private int studentId;
    private String type;
    private String status;
    private String date;
    private String startTime;
    private String endTime;
    private String subject;
    private String reason;
    private String teacherComment;

    public Absence() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getTeacherComment() { return teacherComment; }
    public void setTeacherComment(String teacherComment) { this.teacherComment = teacherComment; }
    public boolean isJustified() { return STATUS_JUSTIFIED.equals(status); }
    public boolean isLate() { return TYPE_LATE.equals(type); }
    public String getDuration() {
        if (startTime != null && endTime != null) return startTime + " - " + endTime;
        return "Journée entière";
    }
}
