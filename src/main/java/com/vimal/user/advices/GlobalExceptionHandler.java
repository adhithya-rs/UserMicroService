package com.vimal.user.advices;

import com.vimal.user.customEceptions.*;
import com.vimal.user.dtos.ErrorDTO;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@org.springframework.web.bind.annotation.ControllerAdvice
public class GlobalExceptionHandler  {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleProductNotFoundException(UserNotFoundException productNotFoundException) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage(productNotFoundException.getMessage());

        return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleGenericException(Exception exception) {
        ErrorDTO errorDTO = new ErrorDTO("An unexpected error occurred.");

        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(UserSaveException.class)
    public ResponseEntity<ErrorDTO> handleUserSaveException(UserSaveException ex) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(DataBaseAccessException.class)
    public ResponseEntity<ErrorDTO> handleDataBaseAccessException(DataBaseAccessException ex) {
        // Log the exception details for debugging purposes
        ErrorDTO errorDTO = new ErrorDTO(ex.getMessage());

        // Return a ResponseEntity object containing the error details and the HTTP status
        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(DuplicateSaveException.class)
    public ResponseEntity<ErrorDTO> handleDuplicateSaveException(DuplicateSaveException ex) {
        // Log the exception details for debugging purposes
        ErrorDTO errorDTO = new ErrorDTO(ex.getMessage());

        // Return a ResponseEntity object containing the error details and the HTTP status
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDTO> handleMethodArgumentMismatchException(MethodArgumentTypeMismatchException ex) {
        // Log the exception details for debugging purposes
        ErrorDTO errorDTO = new ErrorDTO("The parameter in endpoint is invalid");

        // Return a ResponseEntity object containing the error details and the HTTP status
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorDTO> handleEndpointNotPresentException(NoResourceFoundException ex) {
        // Log the exception details for debugging purposes
        ErrorDTO errorDTO = new ErrorDTO("Page not found / The requested endpoint is not available.");

        // Return a ResponseEntity object containing the error details and the HTTP status
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(GenerateTokenException.class)
    public ResponseEntity<ErrorDTO> handleGenerateRegistrationTokenException(GenerateTokenException ex) {
        // Log the exception details for debugging purposes
        ErrorDTO errorDTO = new ErrorDTO("An unknown error occurred.");


        // Return a ResponseEntity object containing the error details and the HTTP status
        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<ErrorDTO> handleInvalidEmailException(InvalidEmailException ex) {
        // Log the exception if needed
        ErrorDTO errorDTO = new ErrorDTO(ex.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST); // 400 BAD REQUEST
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorDTO> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        // Log the exception if needed
        ErrorDTO errorDTO = new ErrorDTO(ex.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.CONFLICT); // 409 CONFLICT
    }

    @ExceptionHandler(SignUpAlreadyInProgressException.class)
    public ResponseEntity<ErrorDTO> handleSignUpAlreadyInProgressException(SignUpAlreadyInProgressException ex) {
        // Log the exception if needed
        ErrorDTO errorDTO = new ErrorDTO(ex.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.CONFLICT); // 409 CONFLICT
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDTO> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        // Handle database-related exceptions gracefully
        ErrorDTO errorDTO = new ErrorDTO(ex.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST); // 400 BAD REQUEST
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorDTO> handleUnauthorizedException(UnauthorizedException ex) {
        // Handle database-related exceptions gracefully
        ErrorDTO errorDTO = new ErrorDTO(ex.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.UNAUTHORIZED); // 401 UNAUTHORIZED
    }

    // Handle PhoneVerificationCodeException
    @ExceptionHandler(PhoneVerificationCodeException.class)
    public ResponseEntity<ErrorDTO> handlePhoneVerificationCodeException(PhoneVerificationCodeException ex) {
        ErrorDTO errorDTO = new ErrorDTO(ex.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.SERVICE_UNAVAILABLE); // 503 Service Unavailable
    }

    // Handle EmailVerificationCodeException
    @ExceptionHandler(EmailVerificationCodeException.class)
    public ResponseEntity<ErrorDTO> handleEmailVerificationCodeException(EmailVerificationCodeException ex) {
        ErrorDTO errorDTO = new ErrorDTO(ex.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.SERVICE_UNAVAILABLE); // 503 Service Unavailable
    }

    // Handle VerificationCodeExpiredException
    @ExceptionHandler(VerificationCodeExpiredException.class)
    public ResponseEntity<ErrorDTO> handleVerificationCodeExpiredException(VerificationCodeExpiredException ex) {
        ErrorDTO errorDTO = new ErrorDTO(ex.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.GONE); // 410 GONE
    }

    // Handle InvalidVerificationCodeException
    @ExceptionHandler(InvalidVerificationCodeException.class)
    public ResponseEntity<ErrorDTO> handleInvalidVerificationCodeException(InvalidVerificationCodeException ex) {
        ErrorDTO errorDTO = new ErrorDTO(ex.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST); // 400 BAD REQUEST
    }

    @ExceptionHandler(UserNotActiveException.class)
    public ResponseEntity<ErrorDTO> handleUserNotActiveException(UserNotActiveException ex) {
        ErrorDTO errorDTO = new ErrorDTO(ex.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.FORBIDDEN); // 403 Forbidden
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorDTO> handleInvalidPasswordException(InvalidPasswordException ex) {
        ErrorDTO errorDTO = new ErrorDTO(ex.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST); // 400 Bad Request
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorDTO> handleExpiredJwtException(ExpiredJwtException ex) {
        ErrorDTO errorDTO = new ErrorDTO(ex.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.UNAUTHORIZED); // 401 Unauthorized
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ErrorDTO> handleMalformedJwtException(MalformedJwtException ex) {
        ErrorDTO errorDTO = new ErrorDTO(ex.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST); // 400 Bad Request
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDTO> handleIllegalArgumentException(IllegalArgumentException ex) {
        // Create a custom error response object
        ErrorDTO errorDTO = new ErrorDTO(ex.getMessage()); // Use the exception's message for the error description
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST); // Return 400 BAD REQUEST status
    }
}
