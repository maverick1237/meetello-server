package com.example.meetelloserver.Rooms;


import com.example.meetelloserver.Users.UserEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "rooms")
@TypeAlias("RoomsTable")
public class RoomsEntity {
    @Id
    private String roomId;

    @NonNull
    private String roomName;

    @NonNull
    private String roomDescription;

    @NonNull
    private String roomType;

    @NonNull
    private UserEntity roomAdmin;

    private List<UserEntity> roomMembers;
}
