package com.teamm.friendstracker.model.entity;

public class User {
    private String email;
    private String name;
    private String surname;
    private String avatar;

    public User() {

    }

    public User(String email, String name, String surname) {
        this.name = name;
        this.email = email;
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
