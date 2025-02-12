package com.git.ilselva.chat.server.service.impl;

import com.git.ilselva.chat.server.model.Room;
import com.git.ilselva.chat.server.model.UserSession;
import com.git.ilselva.chat.server.service.api.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DefaultRoomRepository implements RoomRepository {

    private final ConcurrentHashMap<String, Room> rooms = new ConcurrentHashMap<>();

    @Override
    public Room createRoom(String roomName, UserSession owner) {
        Room room = new Room(roomName, owner);
        rooms.put(roomName, room);
        return room;
    }

    @Override
    public Room getRoom(String roomName) {
        return rooms.get(roomName);
    }

    @Override
    public void deleteRoom(String roomName) {
        rooms.remove(roomName);
    }

    @Override
    public List<String> getAllRooms() {
        return Collections.list(rooms.keys());
    }

}