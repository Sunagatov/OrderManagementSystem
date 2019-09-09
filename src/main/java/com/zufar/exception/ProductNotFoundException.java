package com.zufar.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException() {
        super();
    }
    public ProductNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public ProductNotFoundException(String message) {
        super(message);
    }
    public ProductNotFoundException(Throwable cause) {
        super(cause);
    }
}
