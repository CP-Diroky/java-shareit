package ru.practicum.shareit.user.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.User;

import java.util.Collection;

@UtilityClass
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
