package dev.mlukas.langreader.security;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeOwnAccount(Principal principal) {
        User foundUser = userService.getUser(principal.getName());
        userService.delete(foundUser);
    }
}
