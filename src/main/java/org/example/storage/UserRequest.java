package org.example.storage;

import org.example.models.User;

import java.util.Optional;

public interface UserRequest {
    Optional<User> findById(long id);
}
