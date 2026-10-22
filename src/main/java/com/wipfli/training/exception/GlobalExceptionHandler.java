package com.wipfli.training.exception;

import com.wipfli.training.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PolicyNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handlePolicyNotFound(
            PolicyNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDTO(ex.getPolicyNumber(), ex.getMessage()));
    }

    @ExceptionHandler(DuplicatePolicyNumberException.class)
    public ResponseEntity<ErrorResponseDTO> handleDuplicatePolicyNumber(
            DuplicatePolicyNumberException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponseDTO(ex.getPolicyNumber(), ex.getMessage()));
    }

    @ExceptionHandler(InvalidPolicyDataException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidPolicyData(
            InvalidPolicyDataException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDTO(ex.getPolicyNumber(), ex.getMessage()));
    }

    @ExceptionHandler(IllegalStatusChangeException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalStatusChange(
            IllegalStatusChangeException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponseDTO(ex.getPolicyNumber(), ex.getMessage()));
    }

    @ExceptionHandler(RenewalNotAllowedException.class)
    public ResponseEntity<ErrorResponseDTO> handleRenewalNotAllowed(
            RenewalNotAllowedException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDTO(ex.getPolicyNumber(), ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgument(
            IllegalArgumentException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDTO(null, ex.getMessage()));
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorResponseDTO> handleBadRequest(Exception ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDTO(null, ex.getMessage()));
    }
}
