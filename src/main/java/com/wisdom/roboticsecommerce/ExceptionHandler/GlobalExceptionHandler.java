
package com.wisdom.roboticsecommerce.ExceptionHandler;

import com.wisdom.roboticsecommerce.Security.jwt.JwtEntryPoint;
import com.wisdom.roboticsecommerce.Utils.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleMyCustomException(CustomException e) {
        return ResponseUtil.fail(e.getMessage());
    }

    @ExceptionHandler(LogOnlyException.class)
    public ResponseEntity<?> handleLogOnlyException(LogOnlyException e) {
        logger.error(e.getStackTrace()[0] + " | " + e.getMessage());
        return ResponseUtil.error();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAnotherCustomException(Exception e) {
        e.printStackTrace();
        return ResponseUtil.error();
    }
}