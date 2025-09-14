package com.git.ilselva.chat.server.model;

public class User {

    private String username;
    private String passwordHash;

    public User(String username){
        this.username = username;
        //TODO implement password
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

}
