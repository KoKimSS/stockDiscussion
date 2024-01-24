package com.kss.stockDiscussion.service;

import com.kss.stockDiscussion.repository.userRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    public boolean isExistEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
