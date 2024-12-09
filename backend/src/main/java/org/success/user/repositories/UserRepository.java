package org.success.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.success.user.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {
    Optional<User> findByUsername(String username);
}
