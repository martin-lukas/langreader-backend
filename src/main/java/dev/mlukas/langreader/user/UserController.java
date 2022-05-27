package dev.mlukas.langreader.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/active")
    public ActiveUserResponse getActiveUser(Principal principal) {
        User foundUser = userService.getUser(principal.getName());
        return new ActiveUserResponse(foundUser);
    }
}
