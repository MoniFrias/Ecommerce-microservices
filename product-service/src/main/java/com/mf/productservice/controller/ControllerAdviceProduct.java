package com.mf.productservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mf.productservice.dto.response.Response;
import com.mf.productservice.exceptions.ValidationException;

@RestControllerAdvice
public class ControllerAdviceProduct {

	@ExceptionHandler(value = ValidationException.class)
	public ResponseEntity<Response> validationProduct(final ValidationException validation){
		Response response =  new Response(false, validation.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
}
