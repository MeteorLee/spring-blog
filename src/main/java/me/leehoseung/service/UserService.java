    package me.leehoseung.service;

    import lombok.RequiredArgsConstructor;
    import me.leehoseung.domain.User;
    import me.leehoseung.dto.AddUserRequest;
    import me.leehoseung.repository.UserRepository;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;

    @RequiredArgsConstructor
    @Service
    public class UserService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;

        public Long save(AddUserRequest dto) {
            return userRepository.save(User.builder()
                    .email(dto.getEmail())
                    .password(passwordEncoder.encode(dto.getPassword()))
                    .build()).getId();
        }

        public User findById(long userId) {
            return userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
        }

    }
