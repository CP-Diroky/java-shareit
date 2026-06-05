package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.ConditionsNotMetException;
import ru.practicum.shareit.exceptions.DuplicatedDataEception;
import ru.practicum.shareit.exceptions.NotFoundException;

import java.util.Collection;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public User addUser(User user) {
        if (user == null) throw new ConditionsNotMetException("Неверный ввод!");
        else if (user.getEmail() == null) throw new ConditionsNotMetException("Почта не должна быть пустой!");
        else if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new DuplicatedDataEception("Пользователь с такой почтой уже есть!");
        }
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

    @Transactional
    @Override
    public User updateUser(User user, Long userId) {
        if (user == null) throw new ConditionsNotMetException("Неверный ввод!");
        Optional<User> userWithSameEmail = userRepository.findByEmail(user.getEmail());
        if (userWithSameEmail.isPresent() && !userWithSameEmail.get().getId().equals(userId)) {
            throw new DuplicatedDataEception("Пользователь с такой почтой уже есть!");
        }
        User newUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден!"));
        if (user.getName() != null && !user.getName().isBlank()) newUser.setName(user.getName());
        if (user.getEmail() != null && !user.getEmail().isBlank()) newUser.setEmail(user.getEmail());
        return userRepository.save(newUser);
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
