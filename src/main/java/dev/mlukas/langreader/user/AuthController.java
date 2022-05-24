package dev.mlukas.langreader.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final RoleService roleService;

    public AuthRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostMapping("/signin")
    public void authenticateUser(@Valid @RequestBody SigninRequest signinRequest) {
        // TODO: Should this be empty?
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        if (userService.exists(signupRequest.username())) {
            throw new IllegalArgumentException("Error: Username is already taken!");
        }

        // TODO: Encode the password with Spring Security
        User user = new User(signupRequest.username(), signupRequest.password());
        // Set user role as USER automatically, and admins can be made by hand
        user.setRoles(Set.of(roleService.getRole(RoleType.USER)));
        user.setNativeLang(signupRequest.nativeLang());
        userService.save(user);
    }
}