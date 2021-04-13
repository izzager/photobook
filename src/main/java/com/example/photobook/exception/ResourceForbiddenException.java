package com.example.photobook.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ResourceForbiddenException extends RuntimeException {
    public ResourceForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceForbiddenException(String message) {
        super(message);
    }
}
