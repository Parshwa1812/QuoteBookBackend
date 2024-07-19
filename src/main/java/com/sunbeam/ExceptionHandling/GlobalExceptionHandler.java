package com.sunbeam.ExceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;

import io.jsonwebtoken.ExpiredJwtException;

import java.time.LocalDateTime;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UserExceptions.class)
	public ResponseEntity<ExceptionResponse> userException(UserExceptions ex) {
		ExceptionResponse res = new ExceptionResponse(ex.getMessage(), "User Exception", HttpStatus.BAD_REQUEST);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ExceptionResponse> handleConstraintViolationException(ConstraintViolationException ex) {
		String errorMessage = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + " " + violation.getMessage())
                .findFirst()
                .orElse("Validation failed. Please check your request data.");
		ExceptionResponse res = new ExceptionResponse(errorMessage, "Validation Exception", HttpStatus.BAD_REQUEST);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
	}

	@ExceptionHandler(QuoteException.class)
	public ResponseEntity<ExceptionResponse> QuoteException(QuoteException ex) {
		ExceptionResponse res = new ExceptionResponse(ex.getMessage(), "Quote Exception", HttpStatus.BAD_REQUEST);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
	}

	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<ExceptionResponse> handleExpiredJwtException(ExpiredJwtException ex) {
		
		String errorMessage = "JWT token has expired.";
		ExceptionResponse res = new ExceptionResponse(errorMessage, "JWT Expired", HttpStatus.UNAUTHORIZED);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
	}
	
	@ExceptionHandler(Unauthorized.class)
	public ResponseEntity<ExceptionResponse> handleJwtUnauthorizedException(Unauthorized ex) {
		
		String errorMessage = "Unauthorized access due to invalid JWT.";
		ExceptionResponse res = new ExceptionResponse(errorMessage, "JWT Unauthorized", HttpStatus.UNAUTHORIZED);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ExceptionResponse> handleJwtUnauthorizedException(BadCredentialsException ex) {
		
		String errorMessage = "User Unauthenticated.";
		ExceptionResponse res = new ExceptionResponse(errorMessage, "User Unauthenticated.", HttpStatus.UNAUTHORIZED);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
	}
	

}
