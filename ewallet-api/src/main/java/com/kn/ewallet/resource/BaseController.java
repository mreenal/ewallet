package com.kn.ewallet.resource;

import com.kn.ewallet.exception.HttpException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

public class BaseController {

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<Map<String, String>> handleError(HttpException exception) {
        Map<String, String> body = new HashMap<>();
        body.put("message", exception.getMessage());
        return ResponseEntity.status(exception.getStatus()).body(body);
    }

}
