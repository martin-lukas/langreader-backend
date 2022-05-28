package dev.mlukas.langreader.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

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
    public LoggedInUser authenticateUser(Principal principal) {
        User foundUser = userService.getUser(principal.getName());
        return new LoggedInUser(foundUser);
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
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