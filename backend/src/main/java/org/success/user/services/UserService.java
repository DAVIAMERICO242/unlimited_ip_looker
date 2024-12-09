package org.success.user.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.success.user.DTOs.CreatedUserWithRandomPass;
import org.success.user.entities.User;
import org.success.user.enums.Role;
import org.success.user.repositories.UserRepository;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public CreatedUserWithRandomPass createUserWithRandomPass(String username){
        User user = new User();
        String randomPass = this.generateRandomPassword();
        user.setCreatedAt(LocalDateTime.now());
        user.setUsername(username);
        user.setPassword(new BCryptPasswordEncoder().encode(randomPass));
        user.setRole(Role.USER);
        userRepository.save(user);
        return new CreatedUserWithRandomPass(user,randomPass);
    }

    private String generateRandomPassword(){
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            int index = random.nextInt("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".length()); // Random index for selecting a character
            password.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".charAt(index)); // Append the character to the password
        }
        return password.toString();
    }

    public Boolean doesThisUsernameExist(String username){
        return userRepository.findByUsername(username).isPresent();
    }
}
