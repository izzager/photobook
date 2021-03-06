package com.example.photobook.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ResourceNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String handleNotFound(final ResourceNotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler({ResourceForbiddenException.class})
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public String handleResourceForbidden(final ResourceForbiddenException e) {
        return e.getMessage();
    }

    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(final IllegalArgumentException e) {
        return e.getMessage();
    }

    @ExceptionHandler({IOException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleIOException(final IOException e) {
        return "An error occurred";
    }

    @ExceptionHandler({TimeoutException.class})
    @ResponseStatus(value = HttpStatus.GATEWAY_TIMEOUT)
    public String handleTimeoutException(final TimeoutException e) {
        return "Time is out";
    }

    @ExceptionHandler({BadCredentialsException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public String handleBadCredentialsException(final BadCredentialsException e) {
        return "Bad credentials";
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public String handleUsernameNotFoundException(final UsernameNotFoundException e) {
        return "User not found";
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        return new ResponseEntity<>(Objects.requireNonNull(ex.getFieldError()).getDefaultMessage(), status);
    }

}
