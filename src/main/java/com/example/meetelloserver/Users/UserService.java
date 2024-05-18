package com.example.meetelloserver.Users;

import com.example.meetelloserver.Users.dtos.CreateUserReq;
import com.example.meetelloserver.Users.dtos.LoginUserReq;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
private final UserRepository userRepository;
private final PasswordEncoder passwordEncoder;
private final ModelMapper modelMapper;


public UserService (UserRepository userRepository , ModelMapper modelMapper , PasswordEncoder passwordEncoder ){
    this.modelMapper = modelMapper;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
}


//creating a new USER Sign up
public UserEntity createUser(CreateUserReq req)throws FieldCantBeNullException , UserAlreadyExistException {
        UserEntity newUser = modelMapper.map(req , UserEntity.class);
        if(userRepository.existsByEmail(newUser.getEmail())){
            throw new UserAlreadyExistException();
        }
        newUser.setPassword(passwordEncoder.encode(req.getPassword()));
        return userRepository.save(newUser);
}


//Searching a user using userID
    public UserEntity findUserById(String userId){
        var user = userRepository.findById(userId).orElseThrow(()-> new UserNotFound(userId));
        return user;
    }


    //Login a user using email and password
    public UserEntity loginUser(LoginUserReq req) throws FieldCantBeNullException , UserNotFound {
        if(req.getEmail().isEmpty() || req.getPassword().isEmpty()){
            throw new FieldCantBeNullException();
        }
        var user = userRepository.findByEmail(req.getEmail());
        if(passwordEncoder.matches(req.getPassword() , user.getPassword())){
            return user;
        }
        return null;
    }



    public static class FieldCantBeNullException extends Exception {
        public FieldCantBeNullException(){
            super("One of the required field is empty!");
        }
    }

    public static  class UserAlreadyExistException extends Exception{
        public  UserAlreadyExistException(){
            super("User with same email already exist");
        }
    }

    public static class UserNotFound extends IllegalArgumentException{
        public UserNotFound(String userId){
            super("user with user id :  "+ userId+" Not found");
        }
    }



}
