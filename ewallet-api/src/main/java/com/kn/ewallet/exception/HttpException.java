package com.kn.ewallet.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


public class HttpException extends RuntimeException {

    @Getter
    private HttpStatus status;

    public HttpException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
