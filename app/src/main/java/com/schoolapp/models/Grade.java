package com.schoolapp.models;

public class Grade {
    private int id;
    private int studentId;
    private String subject;
    private float value;
    private float maxValue;
    private float coefficient;
    private float classAverage;
    private String description;
    private String date;
    private String trimester;
    private String teacher;
    private int subjectColor;

    public Grade() {}

    public Grade(int id, int studentId, String subject, float value, float maxValue,
                 float coefficient, float classAverage, String description, String date,
                 String trimester, String teacher) {
        this.id = id;
        this.studentId = studentId;
        this.subject = subject;
        this.value = value;
        this.maxValue = maxValue;
        this.coefficient = coefficient;
        this.classAverage = classAverage;
        this.description = description;
        this.date = date;
        this.trimester = trimester;
        this.teacher = teacher;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public float getValue() { return value; }
    public void setValue(float value) { this.value = value; }
    public float getMaxValue() { return maxValue; }
    public void setMaxValue(float maxValue) { this.maxValue = maxValue; }
    public float getCoefficient() { return coefficient; }
    public void setCoefficient(float coefficient) { this.coefficient = coefficient; }
    public float getClassAverage() { return classAverage; }
    public void setClassAverage(float classAverage) { this.classAverage = classAverage; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getTrimester() { return trimester; }
    public void setTrimester(String trimester) { this.trimester = trimester; }
    public String getTeacher() { return teacher; }
    public void setTeacher(String teacher) { this.teacher = teacher; }
    public int getSubjectColor() { return subjectColor; }
    public void setSubjectColor(int subjectColor) { this.subjectColor = subjectColor; }

    public String getFormattedGrade() {
        return String.format("%.1f / %.0f", value, maxValue);
    }

    public String getGradeCategory() {
        float normalizedGrade = (value / maxValue) * 20;
        if (normalizedGrade >= 16) return "EXCELLENT";
        else if (normalizedGrade >= 12) return "BIEN";
        else if (normalizedGrade >= 10) return "PASSABLE";
        else return "INSUFFISANT";
    }
}
