package com.example.meetelloserver.Rooms.dtos;

import com.example.meetelloserver.Users.UserEntity;
import lombok.Data;
import lombok.NonNull;

@Data
public class CreateRoomReq {
@NonNull private String roomName;
@NonNull private String roomDescription;
@NonNull private String roomType;
private UserEntity userEntity;

}
