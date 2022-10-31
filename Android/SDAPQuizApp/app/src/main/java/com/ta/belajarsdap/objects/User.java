package com.ta.belajarsdap.objects;

public class User {
    private String name;
    private String email;
    private int lessonCompleted;

    public User(String name, String email, int lessonCompleted) {
        this.name = name;
        this.email = email;
        this.lessonCompleted = lessonCompleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getLessonCompleted() {
        return lessonCompleted;
    }

    public void setLessonCompleted(int lessonCompleted) {
        this.lessonCompleted = lessonCompleted;
    }

}
