package com.wisdom.roboticsecommerce.Service;

import com.wisdom.roboticsecommerce.Dto.APIRequest;
import org.springframework.http.ResponseEntity;

public interface APIService {
    ResponseEntity<?> call(APIRequest apiRequest);
}
