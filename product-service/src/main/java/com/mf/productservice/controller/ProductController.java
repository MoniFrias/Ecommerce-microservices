package com.mf.productservice.controller;

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

import com.mf.productservice.dto.request.ProductRequestDTO;
import com.mf.productservice.dto.request.UpdateProductRequestDTO;
import com.mf.productservice.dto.response.CategoryResponseDTO;
import com.mf.productservice.dto.response.ProductResponseDTO;
import com.mf.productservice.dto.response.Response;
import com.mf.productservice.entity.Product;
import com.mf.productservice.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService service;
	
	@PostMapping("/admin/addProduct")
	public ResponseEntity<Response> addProduct(@RequestBody ProductRequestDTO product) throws Exception {
		Response response = service.addProduct(product);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@PostMapping("/admin/addCategory")
	public ResponseEntity<Response> addCategory(@RequestParam String categoryname) throws Exception {
		Response response = service.addCategory(categoryname);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@GetMapping("/getAllProducts")
	public List<ProductResponseDTO> getAllProducts() {
		return service.getAllProducts();
	}
	
	@GetMapping("/getAllCategories")
	public List<CategoryResponseDTO> getAllCategories () {
		return service.getAllCategories();
	}
	
	@GetMapping("/getProductById/{idproduct}")
	public Product getProductById(@PathVariable Long idproduct) {
		return service.getProductById(idproduct);
	}
	
	@GetMapping("/getProductBySku/{sku}")
	public ProductResponseDTO getProductBySku(@PathVariable String sku) {
		return service.getProductBySku(sku);
	}
	
	@GetMapping("/getProductByName/{productName}")
	public ProductResponseDTO getProductByName(@PathVariable String productName) {
		return service.getProductByName(productName);
	}
	
	@GetMapping("/getProductByBrand/{brandName}")
	public List<ProductResponseDTO> getProductByBrand(@PathVariable String brandName) {
		return service.getProductByBrand(brandName);
	}
	
	@PostMapping("/updateStockOrder/{sku}/{quantity}")
	public String updateStockOrder(@PathVariable Long quantity, @PathVariable String sku, @RequestParam String status) {
		return service.updateStockOrder(quantity,sku, status);
	}
	
	@PostMapping("/admin/updateStock/{sku}/{quantity}")
	public String updateStockAdmin(@PathVariable Long quantity, @PathVariable String sku) {
		return service.updateStockAdmin(quantity,sku);
	}
	
	@PutMapping("/admin/updateProduct/{sku}")
	public ResponseEntity<Response> updateProduct(@RequestBody UpdateProductRequestDTO product, @PathVariable String sku) {
		Response response = service.updateProduct(product, sku);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PutMapping("/admin/updateCategory/{categoryId}")
	public ResponseEntity<Response> updateCategory(@RequestParam String categoryname, @PathVariable Long categoryId) {
		Response response = service.updateCategory(categoryname, categoryId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@DeleteMapping("/admin/deleteProduct/{sku}")
	public ResponseEntity<Response> deleteProduct(@PathVariable String sku) {
		Response response = service.deleteProduct(sku);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	
}
