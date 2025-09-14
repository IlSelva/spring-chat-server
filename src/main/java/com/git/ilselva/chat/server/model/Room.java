package com.git.ilselva.chat.server.model;

import java.util.concurrent.ConcurrentHashMap;

public class Room {
    private final String name;
    private final UserSession owner;
    private final ConcurrentHashMap<User, UserSession> members;

    public Room(String name, UserSession owner) {
        this.name = name;
        this.owner = owner;
        this.members = new ConcurrentHashMap<>();
    }

    public void addMember(UserSession userSession) {
        members.put(userSession.getUser(), userSession);
    }

    public void removeMember(UserSession userSession) {
        members.remove(userSession.getUser());
    }

    public boolean isOwner(UserSession userSession) {
        return owner.equals(userSession);
    }

    public void broadcastMessage(String message) {
    }

    public String getName() {
        return name;
    }

    public ConcurrentHashMap<User, UserSession> getMembers() {
        return members;
    }

}