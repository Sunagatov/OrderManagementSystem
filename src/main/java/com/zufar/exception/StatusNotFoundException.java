package com.zufar.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class StatusNotFoundException extends RuntimeException {

    public StatusNotFoundException() {
        super();
    }
    public StatusNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public StatusNotFoundException(String message) {
        super(message);
    }
    public StatusNotFoundException(Throwable cause) {
        super(cause);
    }
}
