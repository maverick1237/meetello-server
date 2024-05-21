package com.example.meetelloserver.Rooms.dtos;

import com.example.meetelloserver.Users.UserEntity;
import lombok.Data;
import lombok.NonNull;

@Data
public class CreateRoomRes {
    private String roomId;
    private String roomName;
    private String roomDescription;
    private UserEntity roomAdmin;
}
