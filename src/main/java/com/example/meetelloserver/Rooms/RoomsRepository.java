package com.example.meetelloserver.Rooms;


import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoomsRepository extends MongoRepository<RoomsEntity, String> {
}
