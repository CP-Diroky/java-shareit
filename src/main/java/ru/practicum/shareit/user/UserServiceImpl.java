package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ConditonsNotMetException;
import ru.practicum.shareit.exceptions.DuplicatedDataEception;
import ru.practicum.shareit.exceptions.NotFoundException;

import java.util.Collection;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User addUser(User user) {
        if (user == null) throw new ConditonsNotMetException("Неверный ввод!");
        else if (user.getEmail() == null) throw new ConditonsNotMetException("Почта не должна быть пустой!");
        else if (userRepository.containsEmail(user.getEmail()))
            throw new DuplicatedDataEception("Пользователь с такой почтой уже есть!");
        return userRepository.saveUser(user);
    }

    @Override
    public Collection<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public User getUserById(Long userId) {
        if (userRepository.getUserById(userId) == null) throw new NotFoundException("Пользователь не найден!");
        return userRepository.getUserById(userId);
    }

    @Override
    public User updateUser(User user, Long userId) {
        if (user == null) throw new ConditonsNotMetException("Неверный ввод!");
        else if (userRepository.containsEmail(user.getEmail()))
            throw new DuplicatedDataEception("Пользователь с такой почтой уже есть!");
        else if (userRepository.getUserById(userId) == null) throw new NotFoundException("Пользователь не найден!");
        return userRepository.updateUser(user, userId);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteUser(userId);
    }
}
