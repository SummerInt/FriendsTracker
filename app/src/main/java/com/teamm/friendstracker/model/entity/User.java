package com.teamm.friendstracker.model.entity;

public class User {
    private String email;
    private String name;
    private String surname;
    private boolean avatar;
    private boolean online;

    public User(){

    }

    public User(String email, String name, String surname, boolean avatar, boolean online) {
        this.name = name;
        this.email = email;
        this.surname = surname;
        this.avatar = avatar;
        this.online = online;
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

    public boolean getAvatar() {
        return avatar;
    }

    public void setAvatar(boolean avatar) {
        this.avatar = avatar;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}
