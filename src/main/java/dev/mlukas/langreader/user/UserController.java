package dev.mlukas.langreader.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/active")
    public ActiveUserResponse getActiveUser() {
        User foundUser = userService.getUser(UserService.MARTIN);
        return new ActiveUserResponse(foundUser);
    }
}
