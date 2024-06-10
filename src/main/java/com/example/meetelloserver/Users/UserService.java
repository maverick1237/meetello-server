package com.example.meetelloserver.Users;

import com.example.meetelloserver.Users.dtos.CreateUserReq;
import com.example.meetelloserver.Users.dtos.LoginUserReq;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetClientRequestException;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.resource.Emailv31;
import org.json.JSONArray;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final MailjetClient mailjetClient;

    @Value("${frontend.url}")
    private  String frontendUrl;

    public UserService(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder,
                       @Value("${mailjet.apikey}") String apiKey,
                       @Value("${mailjet.secretKey}") String secretKey) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailjetClient = new MailjetClient(
                ClientOptions.builder()
                        .apiKey(apiKey)
                        .apiSecretKey(secretKey)
                        .build()
        );
    }

    // Creating a new USER Sign up
    public UserEntity createUser(CreateUserReq req) throws FieldCantBeNullException, UserAlreadyExistException, MailjetException {
        UserEntity newUser = modelMapper.map(req, UserEntity.class);
        UserEntity existedUser = userRepository.findByEmail(newUser.getEmail());
        if (userRepository.existsByEmail(newUser.getEmail()) && existedUser.isActivated()) {
            throw new UserAlreadyExistException();
        } else if (userRepository.existsByEmail(newUser.getEmail()) && !existedUser.isActivated()) {
            sendMagicLink(existedUser);
        }
        newUser.setPassword(passwordEncoder.encode(req.getPassword()));
        newUser.setActivated(false);
        UserEntity savedUser = userRepository.save(newUser);
        sendMagicLink(newUser);
        return savedUser;
    }

    private void sendMagicLink(UserEntity user) throws MailjetException {
        String token = UUID.randomUUID().toString();
        user.setActivationCode(token);
        userRepository.save(user);

        String link = frontendUrl + "/activate/" + token;

        MailjetRequest request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", "iamakki85@gmail.com")
                                        .put("Name", "Meetello"))
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", user.getEmail())
                                                .put("Name", user.getUsername())))
                                .put(Emailv31.Message.SUBJECT, "Activate your Meetello account")
                                .put(Emailv31.Message.TEXTPART, "Click the link to activate your account: " + link)
                                .put(Emailv31.Message.HTMLPART, "<h3>Welcome to Meetello</h3><p>Click <a href=\"" + link + "\">here</a> to activate your account.</p>")));

        try {
            MailjetResponse response = mailjetClient.post(request);
            if (response.getStatus() != 200) {
                throw new MailjetException("Failed to send email: " + response.getStatus() + " - " + response.getData());
            }
        } catch (MailjetClientRequestException e) {
            throw new MailjetException("Error sending email: " + e.getMessage(), e);
        }
    }

    public UserEntity activateUser(String token) throws UserNotFound {
        UserEntity user = userRepository.findByActivationCode(token);

        if (user == null) {
            throw new UserNotFound("Invalid activation token");
        }

        user.setActivated(true);
        user.setActivationCode(null); // Clear the token after activation
        userRepository.save(user);
        return user;
    }

    // Searching a user using userID
    public UserEntity findUserById(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFound(userId));
    }

    // Login a user using email and password
    public UserEntity loginUser(LoginUserReq req) throws FieldCantBeNullException, UserNotFound {
        if (req.getEmail().isEmpty() || req.getPassword().isEmpty()) {
            throw new FieldCantBeNullException();
        }
        UserEntity user = userRepository.findByEmail(req.getEmail());
        if (user == null || !passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new UserNotFound("Invalid email or password");
        }
        if (!user.isActivated()) {
            throw new UserNotFound("User account is not activated");
        }
        return user;
    }

    public static class FieldCantBeNullException extends Exception {
        public FieldCantBeNullException() {
            super("One of the required fields is empty!");
        }
    }

    public static class UserAlreadyExistException extends Exception {
        public UserAlreadyExistException() {
            super("User with the same email already exists");
        }
    }

    public static class UserNotFound extends IllegalArgumentException {
        public UserNotFound(String message) {
            super(message);
        }
    }
}
