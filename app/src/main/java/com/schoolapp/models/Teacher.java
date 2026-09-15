package com.schoolapp.models;

public class Teacher {
    private int id;
    private String firstName;
    private String lastName;
    private String subject;
    private String email;
    private String phone;
    private String avatarUrl;

    public Teacher() {}
    public Teacher(int id, String firstName, String lastName, String subject, String phone) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.subject = subject;
        this.phone = phone;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getFullName() { return "M./Mme " + lastName; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public String getInitials() {
        return String.valueOf(firstName.charAt(0)) + String.valueOf(lastName.charAt(0));
    }
}
