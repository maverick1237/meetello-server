package com.example.meetelloserver.Rooms;


import com.example.meetelloserver.Rooms.dtos.CreateRoomReq;
import com.example.meetelloserver.Rooms.dtos.CreateRoomRes;
import com.example.meetelloserver.Users.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/rooms")
public class RoomsController {
    private final RoomsService roomsService;
    private final ModelMapper modelMapper;

    public RoomsController(RoomsService roomsService, ModelMapper modelMapper) {
        this.roomsService = roomsService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("")
    public ResponseEntity<CreateRoomRes> createNewRoom(@AuthenticationPrincipal UserEntity user , @RequestBody CreateRoomReq req){
        RoomsEntity newRoom = roomsService.createNewRoom(req, user.getUserId());
        CreateRoomRes response = modelMapper.map(newRoom , CreateRoomRes.class);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("")
    ResponseEntity<ArrayList<RoomsEntity>> getAllRooms(){
        var rooms = roomsService.getAllRooms();
        return ResponseEntity.ok().body((ArrayList<RoomsEntity>) rooms);
    }

    @GetMapping("/join/{roomId}")
    public ResponseEntity<RoomsEntity> joinRoom(@AuthenticationPrincipal UserEntity user , @PathVariable String roomId ) throws RoomsService.RoomNotFound {
        var room = roomsService.joinedByUser(roomId , user.getUserId());
        return ResponseEntity.ok().body(room);
    }

    @GetMapping("/delete/{roomId}")
    public ResponseEntity deleteRoom(@PathVariable String roomId) throws RoomsService.RoomNotFound {
        var deletedRoom = roomsService.deleteRoom(roomId);
        return ResponseEntity.ok().body(deletedRoom);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomsEntity> getRoomById(@PathVariable String roomId) throws RoomsService.RoomNotFound{
        var room = roomsService.getRoomById(roomId);
        return  ResponseEntity.ok().body(room);
    }

}
