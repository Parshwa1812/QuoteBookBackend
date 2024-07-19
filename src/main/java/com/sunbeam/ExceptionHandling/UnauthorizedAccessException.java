package com.sunbeam.ExceptionHandling;

public class UnauthorizedAccessException extends RuntimeException {
	public UnauthorizedAccessException(String messege) {
		super(messege);
		}
}
