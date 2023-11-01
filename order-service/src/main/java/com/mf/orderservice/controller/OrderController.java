package com.mf.orderservice.controller;

import java.util.List;

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

import com.mf.orderservice.dto.response.OrderResponseDTO;
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
	
	@CircuitBreaker(name = "createOrder", fallbackMethod = "fallBackOpenfeignClients")
	@PostMapping("/createOrder")
	public OrderResponseDTO createOrder(@RequestParam Long idcart, HttpServletRequest request) {
		return orderService.createOrder(idcart, request.getHeader("Authorization"));
	}
	
	@CircuitBreaker(name = "cancelOrder", fallbackMethod = "fallBackOpenfeignClients")
	@PostMapping("/cancelOrder")
	public String cancelOrder(@RequestParam String ordernumber, HttpServletRequest request) {
		return orderService.cancelOrder(ordernumber, request.getHeader("Authorization"));
	}
	
	@CircuitBreaker(name = "getOrderByOrderNumber", fallbackMethod = "fallBackOpenfeignClients")
	@GetMapping("/getOrderByOrderNumber/{ordernumber}")
	public OrderResponseDTO getOrderByOrderNumber(@PathVariable String ordernumber, HttpServletRequest request) {
		return orderService.getOrderByOrderNumber(ordernumber, request.getHeader("Authorization"));
	}
	
	@CircuitBreaker(name = "getAllorders", fallbackMethod = "fallBackOpenfeignClients")
	@GetMapping("/getAllorders")
	public List<OrderResponseDTO> getAllorders(HttpServletRequest request) {
		return orderService.getAllorders(request.getHeader("Authorization"));
	}
	
	@PostMapping("/updatePayment/{ordernumber}")
	public String updatePayment(@PathVariable String ordernumber, @RequestParam String status) {
		return orderService.updatePayment(ordernumber,status);
	}
	
	@PostMapping("/updateOrder/{ordernumber}")
	public String updateOrder(@PathVariable String ordernumber, @RequestParam String status) {
		return orderService.updateOrder(ordernumber,status);
	}
	
	private ResponseEntity<String> fallBackOpenfeignClients(RuntimeException ex){
		logger.info("Fallback is executed because open feign client is down ", ex.getMessage());
		return new ResponseEntity<>("Oops! Something went wrong, please try again later!", HttpStatus.OK);
	}	
}
