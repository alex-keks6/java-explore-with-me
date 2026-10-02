package ru.practicum.explore.main.mapper;

import lombok.experimental.UtilityClass;
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
}
