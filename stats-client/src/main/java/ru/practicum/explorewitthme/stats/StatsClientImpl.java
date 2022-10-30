package ru.practicum.explorewitthme.stats;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.List;

@Service
public class StatsClientImpl {
    private final RestTemplate rest;

    private static final String API_PREFIX = "/events";

    @Autowired
    public StatsClientImpl(@Value("${explorewithme-stats.url}") String serverUrl, RestTemplateBuilder builder) {
        rest = builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build();
    }

    public ResponseEntity<StatsDto> setViews(long id) {
        return makeAndSendRequest(HttpMethod.POST, "/" + id + "/views");
    }

    public ResponseEntity<StatsDto> setRequests(long id) {
        return makeAndSendRequest(HttpMethod.POST, "/" + id + "/requests");
    }
    public ResponseEntity<StatsDto> getStats(long id) {
        return makeAndSendRequest(HttpMethod.GET, "/" + id);
    }

    private <T> ResponseEntity<StatsDto> makeAndSendRequest(HttpMethod method, String path) {
        HttpEntity<T> requestEntity = new HttpEntity<>(null, defaultHeaders());

        ResponseEntity<StatsDto> statsServerResponse;
        try {
            statsServerResponse = rest.exchange(path, method, requestEntity, StatsDto.class);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new StatsDto());
        }
        return prepareResponse(statsServerResponse);
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    private static ResponseEntity<StatsDto> prepareResponse(ResponseEntity<StatsDto> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());
        return responseBuilder.build();
    }
}
