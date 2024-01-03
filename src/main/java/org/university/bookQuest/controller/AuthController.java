package org.university.bookQuest.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.university.bookQuest.dto.LoginRequest;
import org.university.bookQuest.dto.RegisterRequest;
import org.university.bookQuest.mapper.UserMapper;
import org.university.bookQuest.service.RoleService;
import org.university.bookQuest.service.UserService;

import static org.university.bookQuest.exception.AppExceptionHandler.sendRedirectAndCheckForError;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;
    private final RoleService roleService;

    @GetMapping("/login")
    public ModelAndView showLoginForm(ModelMap map) {
        map.addAttribute("loginRequest", new LoginRequest());
        return new ModelAndView("login", map);
    }

    @PostMapping("/login")
    public void login(@Valid LoginRequest loginRequest, Authentication authentication) {
        var user = userService.readByEmail(loginRequest.getEmail());
        checkPassword(loginRequest.getPassword(), user.getPassword());

        log.info("LOGIN === {} == {}", user.getEmail(), authentication.getPrincipal());
    }

    private void checkPassword(String rawPass, String encodedPass) {
        if (!passwordEncoder.matches(rawPass, encodedPass)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong old password!");
        }
    }

    @GetMapping("/register")
    public ModelAndView showRegisterForm(ModelMap map) {
        map.addAttribute("registerRequest", new RegisterRequest());
        return new ModelAndView("register", map);
    }

    @PostMapping("/register")
    public void register(@Valid RegisterRequest registerRequest, Authentication authentication, HttpServletResponse response) {
        var user = userService.create(mapper.getUserFromRegisterRequest(registerRequest),
                roleService.readByName("USER"));

        log.info("REGISTER === {} == {}", user.getEmail(), authentication.getPrincipal());
        sendRedirectAndCheckForError(response, "/api/auth/login");
    }
}
