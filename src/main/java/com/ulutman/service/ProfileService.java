package com.ulutman.service;

import com.ulutman.model.entities.User;
import com.ulutman.model.entities.UserAccount;
import com.ulutman.repository.UserAccontRepository;
import com.ulutman.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {

    private final UserRepository userRepository;
    private final UserAccontRepository userAccontRepository;

    public UserAccount updateUserAccount(Long userId, String username, String name) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Колдонуучу табылган жок"));

        UserAccount userAccount = user.getUserAccount();

        if (userAccount == null) {
            throw new IllegalArgumentException("Жеке кабинет табылган жок");
        }

        userAccount.setEmail(username);
        userAccount.setName(name);

        user.setEmail(username);
        user.setName(name);
        userRepository.save(user);

        return userAccontRepository.save(userAccount);
    }
}

