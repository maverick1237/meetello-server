package com.example.meetelloserver.Users;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "users")
@TypeAlias("UsersTable")
public class UserEntity {

    @Id
    private String userId;

    @NonNull
    private String username;

    @NonNull
    private String email;

    @NonNull
    private String password;

}
