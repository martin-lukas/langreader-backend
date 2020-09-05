package net.langreader.controller;

import net.langreader.dao.UserRepository;
import net.langreader.model.User;
import net.langreader.payload.request.SimpleRequest;
import net.langreader.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/users")
public class UserRestController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAllByOrderByUsername();
        List<User> strippedUsers = new ArrayList<>();
        for (User strippedUser : users) {
            strippedUser.setWords(null);
            strippedUser.setTexts(null);
            strippedUser.setLangs(null);
            strippedUser.setChosenLang(null);
            strippedUsers.add(strippedUser);
        }
        return new ResponseEntity<>(strippedUsers, HttpStatus.OK);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeUserByUsername(@Valid @RequestBody User user) {
        Optional<User> foundUser = userRepository.findByUsername(user.getUsername());
        if (foundUser.isPresent()) {
            userRepository.delete(foundUser.get());
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/self")
    public ResponseEntity<?> removeUserSelf(HttpServletRequest req) {
        String username = jwtUtils.getUsernameFromHttpRequest(req);
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            userRepository.delete(userOpt.get());
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
