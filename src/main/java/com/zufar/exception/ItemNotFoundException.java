package com.zufar.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ItemNotFoundException extends RuntimeException {

    public ItemNotFoundException() {
        super();
    }
    public ItemNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public ItemNotFoundException(String message) {
        super(message);
    }
    public ItemNotFoundException(Throwable cause) {
        super(cause);
    }
}
