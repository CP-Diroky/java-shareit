package ru.practicum.shareit.user.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;

import java.util.Collection;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public class UserMapper {


    public static User toUser(UserDto userDto) {
        return new User(
                userDto.getName(),
                userDto.getEmail()
        );
    }

    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static Collection<UserDto> toUserDtoCollection(Collection<User> users) {
        return users.stream().map(UserMapper::toUserDto).toList();
    }


}
