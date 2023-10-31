package com.mf.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mf.user.dto.response.Response;
import com.mf.user.exceptions.ValidationException;

@RestControllerAdvice
public class ControllerAdviceUsers {

	@ExceptionHandler(value = ValidationException.class)
	public ResponseEntity<Response> validationUsers (final ValidationException validation){
		Response response = new Response(false, validation.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
}
