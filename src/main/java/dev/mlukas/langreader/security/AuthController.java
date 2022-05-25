package dev.mlukas.langreader.security;

import dev.mlukas.langreader.user.User;
import dev.mlukas.langreader.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        if (userService.userExists(signupRequest.username())) {
            throw new UsernameAlreadyExistsException("The username '%s' is unavailable.".formatted(signupRequest.username()));
        }

        // TODO: Encode the password with Spring Security
        User user = new User(signupRequest.username(), signupRequest.password());
        user.setNativeLang(signupRequest.nativeLang());
        userService.save(user);
    }
}