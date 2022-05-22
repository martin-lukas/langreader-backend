package dev.mlukas.langreader.user;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    // TODO: Remove after adding security
    public static final String MARTIN = "martin";
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UserNotFoundException("The user with the username '%s' doesn't exist.".formatted(username))
                );
    }

    public List<User> getAllOrderedByUsername() {
        return userRepository.findAllByOrderByUsername();
    }

    public boolean exists(String username) {
        return userRepository.existsByUsername(username);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }
}
