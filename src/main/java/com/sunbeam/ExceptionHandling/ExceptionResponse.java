package com.sunbeam.ExceptionHandling;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class ExceptionResponse {

	private String details;
	private String reason;
	private HttpStatus status;
	private LocalDateTime time;

	public ExceptionResponse(String details, String reason, HttpStatus status) {
		this.details = details;
		this.reason = reason;
		this.status = status;
		this.time = LocalDateTime.now();
	}
}
