package com.myles.app.ws.exceptions;

import com.myles.app.ws.model.response.ErrorMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class AppExceptionsHandler { // Used to handle Exceptions // Handles specific exception

    @ExceptionHandler(value = {UserServiceException.class})
    public ResponseEntity<Object> handleUserServiceException(UserServiceException ex, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(new Date(), ex.getMessage());
        String message = "This is a message that I just made up";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Message", message);

        return new ResponseEntity<>(errorMessage, headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {Exception.class}) // Handles all other exceptions
    public ResponseEntity handleOtherExceptions(Exception ex, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(new Date(), ex.getMessage());

        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = FirstNameException.class)
    public ResponseEntity handleFirstNameException(Exception ex, WebRequest request) {

        return new ResponseEntity<>("You forgot the first name", new HttpHeaders(), HttpStatus.CONFLICT);
    }
}
