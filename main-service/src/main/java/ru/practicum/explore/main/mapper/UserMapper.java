package ru.practicum.explore.main.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.explore.main.dto.UserDto;
import ru.practicum.explore.main.dto.UserShortDto;
import ru.practicum.explore.main.model.User;

@UtilityClass
public class UserMapper {
    public UserShortDto mapUserToShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public UserDto mapUserToDto(User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}