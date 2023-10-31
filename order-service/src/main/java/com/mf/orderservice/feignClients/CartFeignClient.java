package com.mf.orderservice.feignClients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mf.orderservice.dto.response.Response;
import com.mf.orderservice.model.CartItem;

@FeignClient(name = "cart-service", url = "http://localhost:8083/api/v1/cart")
public interface CartFeignClient {

	@GetMapping("/getallItems")
	public List<CartItem> getallItems (@RequestParam Long idcart, @RequestParam String ordernumber);
	
	@GetMapping("/validateIfExistsCart/{idcart}")
	public boolean validateIfExistsCart (@PathVariable Long idcart);
	
	@DeleteMapping("/cleanCart")
	public ResponseEntity<Response> cleanCart (@RequestParam Long idcart);
	
	@PutMapping("/updateOrderNumber/{idCart}/{ordernumber}")
	public String updateOrderNumber(@PathVariable Long idCart, @PathVariable String ordernumber);
}
