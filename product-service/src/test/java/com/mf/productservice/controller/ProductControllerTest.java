package com.mf.productservice.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.mf.productservice.dto.request.ProductRequestDTO;
import com.mf.productservice.dto.request.UpdateProductRequestDTO;
import com.mf.productservice.dto.response.CategoryResponseDTO;
import com.mf.productservice.dto.response.ProductResponseDTO;
import com.mf.productservice.dto.response.Response;
import com.mf.productservice.entity.Category;
import com.mf.productservice.entity.Product;
import com.mf.productservice.service.ProductService;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {
	
	@InjectMocks
	private ProductController productController;
	
	@Mock
	private ProductService productService;
	
	private Response response;
	private ProductRequestDTO productRequestDTO;
	private List<ProductResponseDTO> listProducts;
	private Product product;
	private ProductResponseDTO productResponseDTO;
	private List<CategoryResponseDTO> listCategories;
	private UpdateProductRequestDTO updateProductRequestDTO;

	@BeforeEach
	void setup() {
		response = new Response(true, "Ok");
		product = new Product(1L, "Rouge Blush", "2387965", "Rouge Blush", "Dior", new BigDecimal(1023.50), 123L, new Category());

		productRequestDTO = ProductRequestDTO.builder().productname("Rouge Blush").idsku("2387965")
				.description("Rouge Blush").brandname("Dior").priceperunit(new BigDecimal(1023.50))
				.stock(123L).categoryname("Makeup").build();
		
		productResponseDTO = new ProductResponseDTO("Rouge Blush", "2387965",  "Dior", new BigDecimal(1023.50), "Makeup");
		listProducts = new ArrayList<>(Arrays.asList(productResponseDTO));
		listCategories = new ArrayList<>(Arrays.asList(new CategoryResponseDTO("Makeup")));
		updateProductRequestDTO = UpdateProductRequestDTO.builder().productname("Rouge Blush")
				.description("Rouge Blush").brandname("Dior").priceperunit(new BigDecimal(1023.50))
				.stock(123L).categoryname("Makeup").build();
	}
	
	@Test
	void addProductTest() throws Exception {
		when(productService.addProduct(any(ProductRequestDTO.class))).thenReturn(response);
		assertEquals(HttpStatus.CREATED, productController.addProduct(productRequestDTO).getStatusCode());
	}
	
	@Test
	void addCategoryTest() throws Exception {
		when(productService.addCategory(anyString())).thenReturn(response);
		assertEquals(HttpStatus.CREATED, productController.addCategory("Makeup").getStatusCode());
	}
	
	@Test
	void getAllProductsTest() throws Exception {
		when(productService.getAllProducts()).thenReturn(listProducts);
		assertEquals("Dior", productController.getAllProducts().get(0).getBrandname());
	}
	
	@Test
	void getAllCategoriesTest() throws Exception {
		when(productService.getAllCategories()).thenReturn(listCategories);
		assertEquals("Makeup", productController.getAllCategories().get(0).getCategoryname());
	}

	@Test
	void getProductByIdTest() throws Exception {
		when(productService.getProductById(anyLong())).thenReturn(product);
		assertEquals("Dior", productController.getProductById(1L).getBrandname());
	}
	
	@Test
	void getProductBySkuTest() throws Exception {
		when(productService.getProductBySku(anyString())).thenReturn(productResponseDTO);
		assertEquals("Dior", productController.getProductBySku("2387965").getBrandname());
	}
	
	@Test
	void getProductByNameTest() throws Exception {
		when(productService.getProductByName(anyString())).thenReturn(productResponseDTO);
		assertEquals("Dior", productController.getProductByName("Rouge Blush").getBrandname());
	}
	
	@Test
	void getProductByBrandTest() throws Exception {
		when(productService.getProductByBrand(anyString())).thenReturn(listProducts);
		assertEquals("Dior", productController.getProductByBrand("Dior").get(0).getBrandname());
	}

	@Test
	void updateStockOrderTest() throws Exception {
		when(productService.updateStockOrder(anyLong(),anyString(),anyString())).thenReturn("Ok");
		assertEquals("Ok", productController.updateStockOrder(1L, "2387965", "createOrder"));
	}
	
	@Test
	void updateStockAdminTest() throws Exception {
		when(productService.updateStockAdmin(anyLong(),anyString())).thenReturn("Ok");
		assertEquals("Ok", productController.updateStockAdmin(1L, "2387965"));
	}
	
	@Test
	void updateProductTest() throws Exception {
		when(productService.updateProduct(any(UpdateProductRequestDTO.class),anyString())).thenReturn(response);
		assertEquals(HttpStatus.OK, productController.updateProduct(updateProductRequestDTO, "2387965").getStatusCode());
	}
	
	@Test
	void updateCategoryTest() throws Exception {
		when(productService.updateCategory(anyString(), anyLong())).thenReturn(response);
		assertEquals(HttpStatus.OK, productController.updateCategory("Makeup", 1L).getStatusCode());
	}

	@Test
	void deleteProductTest() throws Exception {
		when(productService.deleteProduct(anyString())).thenReturn(response);
		assertEquals(HttpStatus.OK, productController.deleteProduct("2387965").getStatusCode());
	}
}
