package dev.mlukas.langreader.security;

import dev.mlukas.langreader.user.ActiveUserResponse;
import dev.mlukas.langreader.user.User;
import dev.mlukas.langreader.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ActiveUserResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        User foundUser = userService.getUser(loginRequest.username());
        return new ActiveUserResponse(foundUser);
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        if (userService.userExists(signupRequest.username())) {
            throw new UsernameAlreadyExistsException("The username '%s' is unavailable.".formatted(signupRequest.username()));
        }

        User user = new User(
                signupRequest.username(),
                passwordEncoder.encode(signupRequest.password())
        );
        user.setNativeLang(signupRequest.nativeLang());
        userService.addUser(user);
    }
}