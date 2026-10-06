package ru.practicum.explore.stats.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.explore.stats.dto.StatsDto;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class StatsClient extends BaseClient {
    private static final String API_PREFIX = "/stats";
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private final String statsUrl;

    @Autowired
    public StatsClient(@Value("${stats-service.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
        this.statsUrl = serverUrl + API_PREFIX;
    }

    public List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

        UriComponentsBuilder pathBuilder = UriComponentsBuilder.fromHttpUrl(statsUrl)
                .queryParam("start", start.format(formatter))
                .queryParam("end", end.format(formatter));

        if (uris != null && !uris.isEmpty()) {
            pathBuilder.queryParam("uris", uris.toArray());
        }

        if (unique != null) {
            pathBuilder.queryParam("unique", unique);
        }

        URI uri = pathBuilder.build().toUri();


        List<StatsDto> statsDtoList = get(uri, new ParameterizedTypeReference<>() {
        });
        return statsDtoList != null ? statsDtoList : List.of();
    }
}