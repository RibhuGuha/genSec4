package com.replicacia.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceExistsException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public ResourceExistsException(String message) {
        super(message);
    }

}
