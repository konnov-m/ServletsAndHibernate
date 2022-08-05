package org.example.storage;

import org.example.models.User;

import java.util.Optional;

public class UserRequestFromDB implements UserRequest{
    CRUD crud ;

    public UserRequestFromDB(CRUD crud) {
        this.crud = crud;
        User user = new User();
        user.setLogin("konnov");
        user.setName("Konnov Misha");
        user.setPassword("pass");
        crud.create(user);
    }
    @Override
    public Optional<User> findById(long id) {
        return Optional.ofNullable(crud.read(id));
    }

}
