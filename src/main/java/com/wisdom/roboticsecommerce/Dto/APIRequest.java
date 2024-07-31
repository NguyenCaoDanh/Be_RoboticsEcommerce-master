package com.wisdom.roboticsecommerce.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class APIRequest {
    private String url;
    private HttpMethod method;
    private HttpHeaders headers;
    private MultiValueMap<String, String> params;
    private Map<String, Object> body;
}
