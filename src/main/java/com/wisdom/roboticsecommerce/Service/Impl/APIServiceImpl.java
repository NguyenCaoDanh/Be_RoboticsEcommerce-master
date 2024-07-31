package com.wisdom.roboticsecommerce.Service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wisdom.roboticsecommerce.Dto.APIRequest;
import com.wisdom.roboticsecommerce.ExceptionHandler.LogOnlyException;
import com.wisdom.roboticsecommerce.Service.APIService;
import com.wisdom.roboticsecommerce.Utils.Utils;
import org.springframework.core.ResolvableType;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Service
public class APIServiceImpl implements APIService {
    @Override
    public ResponseEntity<?> call(APIRequest apiRequest) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(apiRequest.getBody(), apiRequest.getHeaders());

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiRequest.getUrl()).queryParams(apiRequest.getParams());

        try {
            ResponseEntity<?> response = restTemplate.exchange(builder.toUriString(), apiRequest.getMethod(), entity, Object.class);
            return response;
        } catch (HttpClientErrorException e) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(e.getResponseBodyAsString());
                return ResponseEntity.status(e.getStatusCode()).body(jsonNode);
            } catch (JsonProcessingException e1) {
                throw new LogOnlyException(e1.toString());
            }
        }
    }
}
