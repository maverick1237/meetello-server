package com.example.meetelloserver.Rooms;

import com.example.meetelloserver.Rooms.dtos.CreateRoomReq;
import com.example.meetelloserver.Users.UserRepository;
import com.example.meetelloserver.Users.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomsService  {
    private final RoomsRepository roomsRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public RoomsService(RoomsRepository roomsRepository , ModelMapper modelMapper,
                        UserRepository userRepository){
        this.roomsRepository = roomsRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }
    //getting all rooms
    public Iterable<RoomsEntity> getAllRooms(){
        return roomsRepository.findAll();
    }

    //creating a room

    public RoomsEntity createNewRoom(CreateRoomReq req , String adminId){
        var admin = userRepository.findById(adminId).orElseThrow(()-> new UserService.UserNotFound(adminId));
        return roomsRepository.save(
                RoomsEntity.builder()
                        .roomName(req.getRoomName())
                        .roomDescription(req.getRoomDescription())
                        .roomType(req.getRoomType())
                        .roomAdmin(admin)
                        .build()
        );
    }



}
