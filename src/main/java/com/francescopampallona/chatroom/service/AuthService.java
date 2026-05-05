package com.francescopampallona.chatroom.service;

import com.francescopampallona.chatroom.dto.AuthResponse;
import com.francescopampallona.chatroom.dto.RegisterRequest;
import com.francescopampallona.chatroom.dto.UserDto;
import com.francescopampallona.chatroom.model.User;
import com.francescopampallona.chatroom.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        String username = request.getUsername().trim();
        String email = request.getEmail().trim().toLowerCase();
        String password = request.getPassword();

        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username già in uso");
        }

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email già in uso");
        }

        User user = User.builder()
                .username(username)
                .email(email)
                .passwordHash(passwordEncoder.encode(password))
                .build();

        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(savedUser);

        UserDto userDto = UserDto.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .build();

        return new AuthResponse(token, userDto);
    }
}
