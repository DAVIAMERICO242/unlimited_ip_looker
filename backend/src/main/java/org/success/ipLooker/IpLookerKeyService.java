package org.success.ipLooker;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class IpLookerKeyService {

    public String generateRandomKey(){
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(12);
        for (int i = 0; i < 12; i++) {
            int index = random.nextInt("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".length()); // Random index for selecting a character
            password.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".charAt(index)); // Append the character to the password
        }
        return password.toString();
    }
}
