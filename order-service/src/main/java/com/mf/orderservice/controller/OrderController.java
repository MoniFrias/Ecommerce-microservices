package com.mf.orderservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mf.orderservice.dto.response.Response;
import com.mf.orderservice.service.OrderService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;
	private Logger logger = LoggerFactory.getLogger(OrderController.class);
	
	@CircuitBreaker(name = "createOrder", fallbackMethod = "fallback")
	@PostMapping("/createOrder")
	public ResponseEntity<Response> createOrder(@RequestParam Long idcart, HttpServletRequest request) {
		Response response = orderService.createOrder(idcart, request.getHeader("Authorization"));
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@CircuitBreaker(name = "cancelOrder", fallbackMethod = "fallback")
	@PostMapping("/cancelOrder")
	public ResponseEntity<Response> cancelOrder(@RequestParam String ordernumber, HttpServletRequest request) {
		Response response = orderService.cancelOrder(ordernumber, request.getHeader("Authorization"));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@CircuitBreaker(name = "getOrderByOrderNumber", fallbackMethod = "fallback")
	@GetMapping("/getOrderByOrderNumber/{ordernumber}")
	public ResponseEntity<Response> getOrderByOrderNumber(@PathVariable String ordernumber, HttpServletRequest request) {
		Response response = orderService.getOrderByOrderNumber(ordernumber, request.getHeader("Authorization"));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@CircuitBreaker(name = "getAllorders", fallbackMethod = "fallback")
	@GetMapping("/getAllorders")
	public ResponseEntity<Response> getAllorders(HttpServletRequest request) {
		Response response = orderService.getAllorders(request.getHeader("Authorization"));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/updatePayment/{ordernumber}")
	public String updatePayment(@PathVariable String ordernumber, @RequestParam String status) {
		return orderService.updatePayment(ordernumber,status);
	}
	
	@PostMapping("/updateOrder/{ordernumber}")
	public String updateOrder(@PathVariable String ordernumber, @RequestParam String status) {
		return orderService.updateOrder(ordernumber,status);
	}	
	
	private ResponseEntity<String> fallback(RuntimeException ex) {
		logger.info("FallbackOrder is executed because open feign client is down ", ex.getMessage());
		return new ResponseEntity<>("FallbackOrder - Oops! Something went wrong, please try again later!", HttpStatus.NOT_FOUND);
	}
}
