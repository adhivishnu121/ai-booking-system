package com.adhi.aibooking.booking.exception;

import com.adhi.aibooking.booking.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoTableAvailableException.class)
    public ResponseEntity<ErrorResponse> handleNoTableAvailable(
            NoTableAvailableException ex) {

        ErrorResponse response = new ErrorResponse(
                ex.getMessage(),
                ex.getSuggestedSlots()
        );

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
}