package com.example.meetelloserver.Rooms.dtos;

import lombok.Data;
import lombok.NonNull;

@Data
public class CreateRoomRes {

    private String roomName;
    private String roomDescription;
    private String adminId;
}
