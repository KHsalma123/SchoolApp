package com.schoolapp.models;

public class Student {
    private int id;
    private String firstName;
    private String lastName;
    private String className;
    private String schoolName;
    private String avatarUrl;
    private String studentNumber;
    private int parentId;
    private float generalAverage;
    private int totalAbsences;
    private int totalLate;

    public Student() {}

    public Student(int id, String firstName, String lastName, String className,
                   String schoolName, String avatarUrl, String studentNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.className = className;
        this.schoolName = schoolName;
        this.avatarUrl = avatarUrl;
        this.studentNumber = studentNumber;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getFullName() { return firstName + " " + lastName; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    public String getSchoolName() { return schoolName; }
    public void setSchoolName(String schoolName) { this.schoolName = schoolName; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public String getStudentNumber() { return studentNumber; }
    public void setStudentNumber(String studentNumber) { this.studentNumber = studentNumber; }
    public int getParentId() { return parentId; }
    public void setParentId(int parentId) { this.parentId = parentId; }
    public float getGeneralAverage() { return generalAverage; }
    public void setGeneralAverage(float generalAverage) { this.generalAverage = generalAverage; }
    public int getTotalAbsences() { return totalAbsences; }
    public void setTotalAbsences(int totalAbsences) { this.totalAbsences = totalAbsences; }
    public int getTotalLate() { return totalLate; }
    public void setTotalLate(int totalLate) { this.totalLate = totalLate; }
    public String getInitials() {
        return String.valueOf(firstName.charAt(0)) + String.valueOf(lastName.charAt(0));
    }
}
