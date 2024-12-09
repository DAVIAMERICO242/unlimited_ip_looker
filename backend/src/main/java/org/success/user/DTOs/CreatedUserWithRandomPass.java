package org.success.user.DTOs;

import org.success.user.entities.User;

public record CreatedUserWithRandomPass(User createdUser, String rawPassword) {
}
