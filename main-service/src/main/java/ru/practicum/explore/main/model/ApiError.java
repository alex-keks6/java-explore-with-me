package ru.practicum.explore.main.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ApiError {
    // В schemas модели ApiError также есть поле errors типа "массив" с описанием:
    // "список стектрейсов или описания ошибок".
    // А в качестве примера пустой массив. И в ручках также это поле отсутствует.
    // Поэтому не совсем понимаю, зачем оно здесь нужно и нужно ли.
    // Плюс показывать стектрейс пользователю думаю небезопасно или не нужно. 
    // Если что, поправьте и поясните, пожалуйста.
    private HttpStatus status;
    private String reason;
    private String message;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}