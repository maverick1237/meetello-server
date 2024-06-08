package com.example.meetelloserver.Rooms;

import com.example.meetelloserver.Rooms.dtos.CreateRoomReq;
import com.example.meetelloserver.Users.UserRepository;
import com.example.meetelloserver.Users.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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


     //get room by id
    public RoomsEntity getRoomById(String roomId) throws RoomNotFound {
        return roomsRepository.findById(roomId).orElseThrow(()->new RoomNotFound(roomId));
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
                        .roomMembers(new ArrayList<>())
                        .build()
        );
    }


    public RoomsEntity joinedByUser(String roomId , String userId) throws RoomNotFound {
        var room = roomsRepository.findById(roomId).orElseThrow(()-> new RoomNotFound(roomId));
        var user = userRepository.findById(userId).orElseThrow(()-> new UserService.UserNotFound(userId));
        room.getRoomMembers().add(user);
        return roomsRepository.save(room);
    }

    public ResponseEntity deleteRoom(String roomId) throws RoomNotFound {
        var room = roomsRepository.findById(roomId).orElseThrow(()-> new RoomNotFound(roomId));
        roomsRepository.delete(room);
        return ResponseEntity.ok().body("Room with id "+roomId+" deleted successfully");
    }

    public static class  RoomNotFound extends Exception{
        RoomNotFound(String roomId){
            super("Room with id "+roomId+" not found");
        }
    }



}
