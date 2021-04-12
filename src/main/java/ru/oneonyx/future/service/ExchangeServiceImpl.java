package ru.oneonyx.future.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.oneonyx.future.dto.DataDto;



@Service
public class ExchangeServiceImpl implements ExchangeService {
    private final RestTemplate restClient;
    private final HttpHeaders headers;

    public ExchangeServiceImpl(RestTemplate restClient, HttpHeaders headers) {
        this.restClient = restClient;
        this.headers = headers;
    }

    @Override
    public DataDto getData(String Url) {
        ResponseEntity<DataDto> response = restClient.exchange(Url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                DataDto.class);
        return response.getBody();
    }
}
