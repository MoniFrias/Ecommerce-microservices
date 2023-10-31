package com.mf.orderservice.feignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.mf.orderservice.model.Product;


@FeignClient(name = "product-service", url = "http://localhost:8082/api/v1/product")
public interface ProductFeignClient {
	
	@GetMapping("/getProductById/{idproduct}")
	public Product getProductById(@PathVariable Long idproduct, @RequestHeader(value = "Authorization") String token);
	
	@PostMapping("/updateStockOrder/{sku}/{quantity}")
	public String updateStockOrder(@PathVariable Long quantity, @PathVariable String sku, @RequestParam String status, @RequestHeader(value = "Authorization") String token);
	
}
