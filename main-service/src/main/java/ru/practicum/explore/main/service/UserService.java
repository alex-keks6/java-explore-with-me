package ru.practicum.explore.main.service;

import ru.practicum.explore.main.dto.UserDto;
import ru.practicum.explore.main.model.User;
import ru.practicum.explore.main.request.NewUserRequest;

import java.util.List;

public interface UserService {
    List<UserDto> getUsersByAdmin(List<Long> ids, Integer from, Integer size);

    UserDto createUserByAdmin(NewUserRequest newUserRequest);

    void deleteUserByAdmin(Long userId);

    User takeUserById(Long userId);

    void checkExistsUserById(Long userId);
}