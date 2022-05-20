package dev.mlukas.langreader.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/active")
    public ResponseEntity<User> getActiveUser() {
        // TODO: change impl to get info from cookie
        User foundUser = userRepository.findByUsername(UserRepository.MARTIN).orElse(null);
        if (foundUser != null) {
            foundUser.setTexts(null);
            foundUser.setWords(null);
            foundUser.setLangs(null);
        }
        return new ResponseEntity<>(foundUser, HttpStatus.OK);
    }
}
