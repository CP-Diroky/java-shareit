package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();

    @Override
    public User saveUser(User user) {
        user.setId(getNextId());
        emails.add(user.getEmail());
        users.put(user.getId(),user);
        return user;
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public User getUserById(Long userId) {
        return users.get(userId);
    }

    @Override
    public User updateUser(User user, Long userId) {
        User newUser = users.get(userId);
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            emails.remove(newUser.getEmail());
            newUser.setEmail(user.getEmail());
            emails.add(newUser.getEmail());
        }
        if (user.getName() != null && !user.getName().isBlank()) newUser.setName(user.getName());
        return newUser;
    }

    @Override
    public void deleteUser(Long userId) {
        emails.remove(users.get(userId).getEmail());
        users.remove(userId);
    }

    @Override
    public boolean containsEmail(String email) {
        return emails.contains(email);
    }

    private Long getNextId() {
        if (users.isEmpty()) return 1L;
        return users.values().stream()
                .mapToLong(User::getId)
                .max().getAsLong() + 1;
    }
}
