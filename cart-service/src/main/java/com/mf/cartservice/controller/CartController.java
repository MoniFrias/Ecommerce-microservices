package com.mf.cartservice.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mf.cartservice.dto.request.AddProductRequestDto;
import com.mf.cartservice.dto.response.CartResponseDTO;
import com.mf.cartservice.dto.response.Response;
import com.mf.cartservice.entity.CartItem;
import com.mf.cartservice.service.CartService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
	
	private final CartService cartService;
	
	@GetMapping("/validateIfExistsCart/{idcart}")
	public boolean validateIfExistsCart (@PathVariable Long idcart) {
		return cartService.validateIfExistsCart(idcart);
	}
	
	@PostMapping("/createCart")
	public ResponseEntity<Response> createCart (@RequestParam Long iduser) {
		Response response = cartService.createCart(iduser);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}	

	@PostMapping("/addProductToCart")
	public ResponseEntity<Response> addProductToCart (@RequestBody AddProductRequestDto addProductRequestDTO, HttpServletRequest request) {
		Response response = cartService.addProductToCart(addProductRequestDTO, request.getHeader("Authorization"));
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@PostMapping("/increaseQuantity")
	public ResponseEntity<Response> increaseProductQuantityFromCart (@RequestParam Long idcart, @RequestParam Long idproduct, HttpServletRequest request) {
		Response response =  cartService.increaseProductQuantityFromCart(idcart, idproduct, request.getHeader("Authorization"));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping("/decreaseQuantity")
	public ResponseEntity<Response> decreaseProductQuantityFromCart (@RequestParam Long idcart, @RequestParam Long idproduct, HttpServletRequest request) {
		Response response = cartService.decreaseProductQuantityFromCart(idcart, idproduct, request.getHeader("Authorization"));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/getAllCartItems")
	public List<CartResponseDTO> getallCartItems (@RequestParam Long idcart, HttpServletRequest request) {
		return cartService.getallCartItems(idcart, request.getHeader("Authorization"));
	}

	@GetMapping("/getallItems")
	public List<CartItem> getallItems (@RequestParam Long idcart, @RequestParam String ordernumber) {
		return cartService.getallItems(idcart, ordernumber);
	}
	
	@DeleteMapping("/removeItem")
	public ResponseEntity<Response> removeItemFromCart (@RequestParam Long idcart, @RequestParam Long idproduct) {
		Response response = cartService.removeItemFromCart(idcart, idproduct);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PutMapping("/updateOrderNumber/{idCart}/{ordernumber}")
	public String updateOrderNumber(@PathVariable Long idCart, @PathVariable String ordernumber) {
		return cartService.updateOrderNumber(idCart, ordernumber);
	}
	
	@DeleteMapping("/cleanCart")
	public ResponseEntity<Response> cleanCart (@RequestParam Long idcart) {
		Response response = cartService.cleanCart(idcart);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
