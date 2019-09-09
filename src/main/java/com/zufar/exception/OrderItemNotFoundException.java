package com.zufar.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class OrderItemNotFoundException extends RuntimeException {

    public OrderItemNotFoundException() {
        super();
    }
    public OrderItemNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public OrderItemNotFoundException(String message) {
        super(message);
    }
    public OrderItemNotFoundException(Throwable cause) {
        super(cause);
    }
}
