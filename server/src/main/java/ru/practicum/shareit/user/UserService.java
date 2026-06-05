package ru.practicum.shareit.user;


import java.util.Collection;

public interface UserService {

    User addUser(User user);

    Collection<User> getAllUsers();

    User getUserById(Long userId);

    User updateUser(User user, Long userId);

    void deleteUser(Long id);
}
