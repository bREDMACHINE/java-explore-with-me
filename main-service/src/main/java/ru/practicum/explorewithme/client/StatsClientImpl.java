package ru.practicum.explorewithme.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.explorewithme.dto.StatsDto;
import ru.practicum.explorewithme.dto.StatsOutDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class StatsClientImpl {
    private final RestTemplate rest;

    @Autowired
    public StatsClientImpl(@Value("${explorewithme-stats.url}") String serverUrl, RestTemplateBuilder builder) {
        rest = builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build();
    }

    public void setViews(StatsDto statsDto) {
        makeAndSendRequest(HttpMethod.POST, "/hit", statsDto);
    }

    public ResponseEntity<List<StatsOutDto>> getStats(List<String> uris,
                                                LocalDateTime start,
                                                LocalDateTime end,
                                                Boolean unique) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        log.info("Request to stats-server with parameters: /stats?start={}&end={}&uris={}&unique={}",
                start.format(formatter),
                end.format(formatter),
                uris,
                unique);
        ResponseEntity<List<StatsOutDto>> listResponseEntity = makeAndSendRequest(HttpMethod.GET,
                "/stats?start=" + start.format(formatter) + "&end=" + end.format(formatter) + "&uris=" + uris + "&unique=" + unique,
                null);
        log.info("Return listResponseEntity={}", listResponseEntity);
        return listResponseEntity;
    }

    private ResponseEntity<List<StatsOutDto>> makeAndSendRequest(HttpMethod method, String path, StatsDto statsDto) {
        HttpEntity<StatsDto> requestEntity = new HttpEntity<>(statsDto, defaultHeaders());

        ResponseEntity<List<StatsOutDto>> statsServerResponse;
        try {
            statsServerResponse = rest.exchange(path, method, requestEntity, new ParameterizedTypeReference<>() {
            });
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new ArrayList<>());
        }
        return prepareResponse(statsServerResponse);
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    private static ResponseEntity<List<StatsOutDto>> prepareResponse(ResponseEntity<List<StatsOutDto>> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());
        return responseBuilder.build();
    }
}
