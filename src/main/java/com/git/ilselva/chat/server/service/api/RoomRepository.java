package com.git.ilselva.chat.server.service.api;

import com.git.ilselva.chat.server.model.Room;
import com.git.ilselva.chat.server.model.UserSession;

import java.util.List;

public interface RoomRepository {

    Room createRoom(String roomName, UserSession owner);

    Room getRoom(String roomName);

    public void deleteRoom(String roomName);

    public List<String> getAllRooms();

}