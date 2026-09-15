package com.schoolapp.models;

public class ScheduleSlot {
    private int id;
    private int studentId;
    private String subject;
    private String teacher;
    private String room;
    private String startTime;
    private String endTime;
    private String dayOfWeek;
    private int dayIndex;
    private int colorRes;
    private boolean isCancelled;

    public ScheduleSlot() {}

    public ScheduleSlot(int id, String subject, String teacher, String room,
                        String startTime, String endTime, String dayOfWeek, int dayIndex) {
        this.id = id;
        this.subject = subject;
        this.teacher = teacher;
        this.room = room;
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayOfWeek = dayOfWeek;
        this.dayIndex = dayIndex;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getTeacher() { return teacher; }
    public void setTeacher(String teacher) { this.teacher = teacher; }
    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public int getDayIndex() { return dayIndex; }
    public void setDayIndex(int dayIndex) { this.dayIndex = dayIndex; }
    public int getColorRes() { return colorRes; }
    public void setColorRes(int colorRes) { this.colorRes = colorRes; }
    public boolean isCancelled() { return isCancelled; }
    public void setCancelled(boolean cancelled) { isCancelled = cancelled; }
    public String getTimeRange() { return startTime + " - " + endTime; }
    public String getInitial() { return subject.length() > 0 ? String.valueOf(subject.charAt(0)) : "?"; }
}
