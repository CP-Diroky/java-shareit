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
        else if (!userRepository.findByEmailContaining(user.getEmail()).isEmpty())
            throw new DuplicatedDataEception("Пользователь с такой почтой уже есть!");
        return userRepository.save(user);
    }

    @Override
    public Collection<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long userId) {
        if (userRepository.findById(userId).isEmpty()) throw new NotFoundException("Пользователь не найден!");
        return userRepository.findById(userId).get();
    }

    @Override
    public User updateUser(User user, Long userId) {
        if (user == null) throw new ConditonsNotMetException("Неверный ввод!");
        else if (!userRepository.findByEmailContaining(user.getEmail()).isEmpty())
            throw new DuplicatedDataEception("Пользователь с такой почтой уже есть!");
        User newUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден!"));
        if (user.getName() != null && !user.getName().isBlank()) newUser.setName(user.getName());
        if (user.getEmail() != null && !user.getEmail().isBlank()) newUser.setEmail(user.getEmail());
        return userRepository.save(newUser);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
