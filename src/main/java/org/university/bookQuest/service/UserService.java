package org.university.bookQuest.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.university.bookQuest.entity.Role;
import org.university.bookQuest.entity.User;
import org.university.bookQuest.repository.UserRepository;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public User create(User user, Role role) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(role);
        userRepository.saveAndFlush(user);
        log.info("registered - {}", user);
        return user;
    }

    public User readById(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found!"));
        log.info("read user with email {}", user.getEmail());
        return user;
    }

    public User readByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found!"));
        log.info("read user with email {}", user.getEmail());
        return user;
    }

    public void delete(long id) {
        userRepository.delete(readById(id));
        log.info("user with id {} successfully deleted", id);
    }

    public boolean isAdmin(long id) {
        return roleService.getByUserId(id).getName().equals("ADMIN");
    }
}
