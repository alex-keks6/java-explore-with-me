package ru.practicum.explore.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explore.main.dto.UserDto;
import ru.practicum.explore.main.exception.DataNotFoundException;
import ru.practicum.explore.main.mapper.UserMapper;
import ru.practicum.explore.main.model.User;
import ru.practicum.explore.main.repository.UserRepository;
import ru.practicum.explore.main.request.NewUserRequest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getUsersByAdmin(List<Long> ids, Integer from, Integer size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        List<User> userList = userRepository.findAllByIds(ids, page).getContent();
        return userList.stream()
                .map(UserMapper::mapUserToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto createUserByAdmin(NewUserRequest newUserRequest) {
        User user = User.builder()
                .email(newUserRequest.getEmail())
                .name(newUserRequest.getName())
                .build();
        return UserMapper.mapUserToDto(userRepository.save(user));
    }

    @Override
    public void deleteUserByAdmin(Long userId) {
        checkExistsUserById(userId);
        userRepository.deleteById(userId);
    }

    @Override
    public User takeUserById(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new DataNotFoundException("User with id=" + userId + " was not found");
        }
        return optionalUser.get();
    }

    @Override
    public void checkExistsUserById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new DataNotFoundException("User with id=" + userId + " was not found");
        }
    }
}
