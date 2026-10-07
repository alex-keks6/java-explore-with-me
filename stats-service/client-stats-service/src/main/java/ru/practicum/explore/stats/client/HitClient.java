package ru.practicum.explore.stats.client;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.explore.stats.dto.HitDto;

import java.time.LocalDateTime;

@Service
public class HitClient extends BaseClient {
    private static final String API_PREFIX = "/hit";
    private final String app;

    @Autowired
    public HitClient(@Value("${stats-service.url}") String serverUrl,
                     @Value("${main-service.name}") String app, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
        this.app = app;
    }

    public void saveHit(HttpServletRequest request) {
        HitDto hitDto = HitDto.builder()
                .app(app)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build();
        post("", hitDto);
    }
}