package com.mf.productservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mf.productservice.dto.Mapper;
import com.mf.productservice.dto.request.ProductRequestDTO;
import com.mf.productservice.dto.request.UpdateProductRequestDTO;
import com.mf.productservice.dto.response.CategoryResponseDTO;
import com.mf.productservice.dto.response.ProductResponseDTO;
import com.mf.productservice.entity.Category;
import com.mf.productservice.entity.Product;
import com.mf.productservice.exceptions.ValidationException;
import com.mf.productservice.repository.RepositoryCategory;
import com.mf.productservice.repository.RepositoryProduct;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
	
	@InjectMocks
	private ProductService productService;
	
	@Mock
	private RepositoryProduct repositoryProduct;
	
	@Mock
	private RepositoryCategory repositoryCategory;
	
	@Mock
	private Mapper mapper;
	
	private Product product;
	private Category category;
	private ProductRequestDTO productRequestDTO;
	private ProductResponseDTO productResponseDTO;
	private UpdateProductRequestDTO updateProductRequestDTO;
	private List<Product> listProduct = new ArrayList<>();
	private List<Category> listCategories = new ArrayList<>();

	@BeforeEach
	void setup() {
		product = new Product(1L, "Rouge Blush", "2387965", "Rouge Blush", "Dior", new BigDecimal(1023.50), 123L, new Category(1L, "Makeup", listProduct));
		category = new Category(1L, "Makeup", new ArrayList<>(Arrays.asList(product)));
		productRequestDTO = ProductRequestDTO.builder().productname("Rouge Blush").idsku("2387965").description("Rouge Blush")
				.brandname("Dior").priceperunit(new BigDecimal(1023.50)).stock(1233L).categoryname("Makeup").build();
		listProduct.add(product);
		listCategories.add(category);
		productResponseDTO = new ProductResponseDTO("Rouge Blush","2387965","Dior",new BigDecimal(1023.50),"Makeup");
				
		updateProductRequestDTO = UpdateProductRequestDTO.builder()
				.productname("Rouge Blush").description("Rouge Blush").brandname("Dior")
				.priceperunit(new BigDecimal(1023.50)).stock(1233L).categoryname("Makeup").build();
	}
	
	@Test
	void testAddProduct() throws Exception {
		when(repositoryProduct.findProductByProductname(anyString())).thenReturn(null);
		when(repositoryCategory.findCategoryByCategoryname(anyString())).thenReturn(category);
		assertEquals("Product was created successfully", productService.addProduct(productRequestDTO).getMessage());
	}
	
	@Test
	void testAddProduct_productnotNull() throws Exception {
		when(repositoryProduct.findProductByProductname(anyString())).thenReturn(product);
		assertThrows(ValidationException.class, ()->{
			productService.addProduct(productRequestDTO);
		});
		verify(repositoryProduct, never()).save(product);
	}
	
	@Test
	void testAddProduct_categoryNotFound() throws Exception {
		when(repositoryProduct.findProductByProductname(anyString())).thenReturn(null);
		when(repositoryCategory.findCategoryByCategoryname(anyString())).thenReturn(null);
		assertThrows(ValidationException.class, ()->{
			productService.addProduct(productRequestDTO);
		});
		verify(repositoryProduct, never()).save(product);
	}

	@Test
	void testAddCategory() throws Exception {
		when(repositoryCategory.findCategoryByCategoryname(anyString())).thenReturn(null);
		assertEquals("Category was created successfully", productService.addCategory("Makeup").getMessage());
	}
	
	@Test
	void testAddCategory_categoryNotNull() throws Exception {
		when(repositoryCategory.findCategoryByCategoryname(anyString())).thenReturn(category);
		assertThrows(ValidationException.class, ()->{
			productService.addCategory("Makeup");
		});
		verify(repositoryCategory, never()).save(category);
	}

	@Test
	void testGetAllProducts() {
		when(repositoryProduct.findAll()).thenReturn(listProduct);
		when(repositoryCategory.findCategoryByIdcategory(product.getCategory().getIdcategory())).thenReturn(category);
		when(mapper.productToProductResponseDto(product, "Makeup")).thenReturn(productResponseDTO);
		assertEquals("Makeup", productService.getAllProducts().get(0).getCategoryname());
	}
	
	@Test
	void testGetAllProducts_listEmopty() {
		when(repositoryProduct.findAll()).thenReturn(new ArrayList<>());
		assertThrows(ValidationException.class, ()->{
			productService.getAllProducts();
		});
	}

	@Test
	void testGetAllCategories() {
		CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO("Makeup");
		when(repositoryCategory.findAll()).thenReturn(listCategories);
		when(mapper.categoryToCategoryResponseDto(category)).thenReturn(categoryResponseDTO);
		assertEquals("Makeup", productService.getAllCategories().get(0).getCategoryname());
	}
	
	@Test
	void testGetAllCategories_empty() {
		when(repositoryCategory.findAll()).thenReturn(new ArrayList<>());
		assertThrows(ValidationException.class, ()->{
			productService.getAllCategories();
		});
	}

	@Test
	void testGetProductById() {
		when(repositoryProduct.findById(anyLong())).thenReturn(Optional.of(product));
		assertEquals("Rouge Blush", productService.getProductById(1L).getProductname());
	}
	
	@Test
	void testGetProductById_NoProduct() {
		when(repositoryProduct.findById(anyLong())).thenReturn(Optional.empty());
		assertThrows(ValidationException.class, ()->{
			productService.getProductById(1L);
		});
	}

	@Test
	void testGetProductBySku() {
		when(repositoryProduct.findProductByIdsku(anyString())).thenReturn(product);
		when(repositoryCategory.findCategoryByIdcategory(product.getCategory().getIdcategory())).thenReturn(category);
		when(mapper.productToProductResponseDto(product, "Makeup")).thenReturn(productResponseDTO);
		assertEquals("Makeup" ,productService.getProductBySku("2387965").getCategoryname());
	}
	
	@Test
	void testGetProductBySku_productNull() {
		when(repositoryProduct.findProductByIdsku(anyString())).thenReturn(null);
		assertThrows(ValidationException.class, ()->{
			productService.getProductBySku("2387965");
		});
	}

	@Test
	void testGetProductByName() {
		when(repositoryProduct.findProductByProductname(anyString())).thenReturn(product);
		when(repositoryCategory.findCategoryByIdcategory(anyLong())).thenReturn(category);
		when(mapper.productToProductResponseDto(product, "Makeup")).thenReturn(productResponseDTO);
		assertEquals("Makeup", productService.getProductByName("Rouge Blush").getCategoryname());
	}
	
	@Test
	void testGetProductByName_productNull() {
		when(repositoryProduct.findProductByProductname(anyString())).thenReturn(null);
		assertThrows(ValidationException.class, ()->{
				productService.getProductByName("Rouge Blush");
		});
	}

	@Test
	void testGetProductByBrand() {
		when(repositoryProduct.findProductsByBrandname(anyString())).thenReturn(listProduct);
		when(repositoryCategory.findCategoryByIdcategory(anyLong())).thenReturn(category);
		when(mapper.productToProductResponseDto(product, "Makeup")).thenReturn(productResponseDTO);
		assertEquals("Makeup", productService.getProductByBrand("Dior").get(0).getCategoryname());
	}
	
	@Test
	void testGetProductByBrand_productsEmpty() {
		when(repositoryProduct.findProductsByBrandname(anyString())).thenReturn(new ArrayList<>());
		assertThrows(ValidationException.class, () -> {
			productService.getProductByBrand("Dior");
		});
	}

	@Test
	void testUpdateStockAdmin() {
		when(repositoryProduct.findProductByIdsku(anyString())).thenReturn(product);
		assertEquals("The stock was updated to: 1", productService.updateStockAdmin(1L, "2387965"));
	}
	
	@Test
	void testUpdateStockAdmin_productNull() {
		when(repositoryProduct.findProductByIdsku(anyString())).thenReturn(null);
		assertThrows(ValidationException.class, () -> {
			productService.updateStockAdmin(1L, "2387965");
			});
		verify(repositoryProduct, never()).save(product);
	}

	@Test
	void testUpdateStockOrder_createOrder() {
		when(repositoryProduct.findProductByIdsku(anyString())).thenReturn(product);
		assertEquals("The stock was updated from: 123 to: 122", productService.updateStockOrder(1L, "2387965", "createOrder"));
	}
	
	@Test
	void testUpdateStockOrder_cancelOrder() {
		when(repositoryProduct.findProductByIdsku(anyString())).thenReturn(product);
		assertEquals("The stock was updated from: 123 to: 124", productService.updateStockOrder(1L, "2387965", "cancelOrder"));
	}
	
	@Test
	void testUpdateStockOrder_productNull_invalidOption() {
		when(repositoryProduct.findProductByIdsku(anyString())).thenReturn(product);
		assertThrows(ValidationException.class, () ->  {
			productService.updateStockOrder(1L, "2387965", "Other");
		});
	}
	
	@Test
	void testUpdateStockOrder_productNull() {
		when(repositoryProduct.findProductByIdsku(anyString())).thenReturn(null);
		assertThrows(ValidationException.class, () ->  {
			productService.updateStockOrder(1L, "2387965", "createOrder");
		});
	}

	@Test
	void testUpdateProduct() {		
		when(repositoryProduct.findProductByIdsku(anyString())).thenReturn(product);
		when(repositoryCategory.findCategoryByIdcategory(anyLong())).thenReturn(category);
		assertTrue(productService.updateProduct(updateProductRequestDTO, "2387965").isResult());
	}
	
	@Test
	void testUpdateProduct_categoryNull() {		
		when(repositoryProduct.findProductByIdsku(anyString())).thenReturn(product);
		when(repositoryCategory.findCategoryByIdcategory(anyLong())).thenReturn(null);
		assertThrows(ValidationException.class, () -> {
			productService.updateProduct(updateProductRequestDTO, "2387965");
		});
		verify(repositoryProduct, never()).save(product);
	}
	
	@Test
	void testUpdateProduct_productNull() {		
		when(repositoryProduct.findProductByIdsku(anyString())).thenReturn(null);
		assertThrows(ValidationException.class, () -> {
			productService.updateProduct(updateProductRequestDTO, "2387965");
		});
		verify(repositoryProduct, never()).save(product);
	}

	@Test
	void testUpdateCategory() {
		when(repositoryCategory.findCategoryByIdcategory(anyLong())).thenReturn(category);
		assertTrue(productService.updateCategory("Makeup", 1L).isResult());
	}
	
	@Test
	void testUpdateCategory_catergoryNull() {
		when(repositoryCategory.findCategoryByIdcategory(anyLong())).thenReturn(null);
		assertThrows(ValidationException.class, () -> {
			productService.updateCategory("Makeup", 1L);
		});
		verify(repositoryProduct, never()).save(product);
	}

	@Test
	void testDeleteProduct() {
		when(repositoryProduct.findProductByIdsku(anyString())).thenReturn(product);
		assertTrue(productService.deleteProduct("2387965").isResult());
	}
	
	@Test
	void testDeleteProduct_productNull() {
		when(repositoryProduct.findProductByIdsku(anyString())).thenReturn(null);
		assertThrows(ValidationException.class, () -> {
			productService.deleteProduct("2387965");
		});
		verify(repositoryProduct, never()).deleteById(1L);
	}

}
