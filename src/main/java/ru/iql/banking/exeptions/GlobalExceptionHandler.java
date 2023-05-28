package ru.iql.banking.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.iql.banking.dtos.errors.ErrorResponse;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({BankingUserNotFoundException.class, EmailNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundExceptions(Exception e) {
        return createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler({NoArgumentsSpecified.class, ConstraintViolationException.class,
            InvalidTransferAmountException.class, EmailIsExistException.class,
            PhoneIsExistException.class, OnlyOneInstanceOfData.class})
    public ResponseEntity<ErrorResponse> handleBadRequestException(Exception e) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .reasonPhrase(httpStatus.getReasonPhrase())
                .status(httpStatus.value())
                .errorMessage(message)
                .build(), httpStatus);
    }
}
