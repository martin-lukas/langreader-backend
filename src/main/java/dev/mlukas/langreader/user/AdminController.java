package dev.mlukas.langreader.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllOrderedByUsername();
        List<User> strippedUsers = new ArrayList<>();
        for (User strippedUser : users) {
            strippedUser.setWords(Collections.emptyList());
            strippedUser.setTexts(Collections.emptyList());
            strippedUser.setLangs(Collections.emptyList());
            strippedUser.setChosenLang(null);
            strippedUsers.add(strippedUser);
        }
        return new ResponseEntity<>(strippedUsers, HttpStatus.OK);
    }

    @DeleteMapping("/users")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUserByUsername(@RequestBody String username) {
        User foundUser = userService.getUser(username);
        userService.delete(foundUser);
    }

    @DeleteMapping("/users/self")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUserSelf(HttpServletRequest req) {
        User foundUser = userService.getUser(UserService.MARTIN);
        userService.delete(foundUser);
    }
}
