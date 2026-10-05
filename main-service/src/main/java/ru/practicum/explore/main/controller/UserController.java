package ru.practicum.explore.main.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.main.dto.UserDto;
import ru.practicum.explore.main.request.NewUserRequest;
import ru.practicum.explore.main.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @GetMapping("/admin/users")
    public List<UserDto> getUsersByAdmin(@RequestParam(required = false) List<@Positive Long> ids,
                                         @PositiveOrZero
                                         @RequestParam(required = false, defaultValue = "0") Integer from,
                                         @Positive
                                         @RequestParam(required = false, defaultValue = "10") Integer size) {
        return userService.getUsersByAdmin(ids, from, size);
    }

    @PostMapping("/admin/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUserByAdmin(@Valid @RequestBody NewUserRequest newUserRequest) {
        return userService.createUserByAdmin(newUserRequest);
    }

    @DeleteMapping("/admin/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserByAdmin(@Positive @PathVariable Long userId) {
        userService.deleteUserByAdmin(userId);
    }

}