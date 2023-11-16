package com.mf.cartservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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

import com.mf.cartservice.dto.Mapper;
import com.mf.cartservice.dto.request.AddProductRequestDto;
import com.mf.cartservice.dto.response.CartResponseDTO;
import com.mf.cartservice.entity.Cart;
import com.mf.cartservice.entity.CartItem;
import com.mf.cartservice.exception.ValidationException;
import com.mf.cartservice.feignClients.ProductFeignClient;
import com.mf.cartservice.feignClients.UserFeignClient;
import com.mf.cartservice.model.Category;
import com.mf.cartservice.model.Product;
import com.mf.cartservice.model.Role;
import com.mf.cartservice.model.User;
import com.mf.cartservice.repository.CartItemRepository;
import com.mf.cartservice.repository.CartRepository;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {
	
	@InjectMocks
	private CartService cartService;
	
	@Mock
	private CartRepository cartRepository;
	
	@Mock
	private CartItemRepository cartItemRepository;
	
	@Mock
	private UserFeignClient userFeignClient;
	
	@Mock
	private ProductFeignClient productFeignClient;
	
	@Mock
	private Mapper mapper; 
	
	private User user;
	private Cart cart;
	private Product product;
	private AddProductRequestDto addProductRequestDto;
	private CartItem cartItem;
	private List<CartItem> listCartItem;

	@BeforeEach
	void setup() {
		user = new User(1L, "juan", "lopez", "juan@email", "122", Role.USER);
		cart = new Cart(1L, 1L, null);
		product = new Product(1L, "Rouge Blush", "2387965", "Rouge Blush", "Dior", new BigDecimal(1023.50), 123L, new Category());
		addProductRequestDto = AddProductRequestDto.builder().idcart(1L).idproduct(1L).productquantity(1L).build();
		cartItem = new CartItem(1L, 1L, 1L, new BigDecimal(123.44), "E23102013425519", cart); 
		listCartItem = new ArrayList<>(Arrays.asList(cartItem));
	}
	
	@Test
	void createTest() {		
		when(userFeignClient.getUserById(anyLong())).thenReturn(user);
		when(cartRepository.findByIdUser(anyLong())).thenReturn(null);
		assertEquals("Cart created", cartService.createCart(1L).getMessage());
	}
	
	@Test
	void createTest_existingCartNonNull() {
		when(userFeignClient.getUserById(anyLong())).thenReturn(user);
		when(cartRepository.findByIdUser(anyLong())).thenReturn(cart);
		assertThrows(ValidationException.class, () -> {
			 cartService.createCart(1L);
		});
		verify(cartRepository, never()).save(any(Cart.class));
	}
	
	@Test
	void createTest_userNull() {
		when(userFeignClient.getUserById(anyLong())).thenReturn(null);
		assertThrows(ValidationException.class, () -> {
			 cartService.createCart(1L);
		});
		verify(cartRepository, never()).save(any(Cart.class));
	}
	
	
	@Test
	void addProductToCartTest_existingCartItemsEmpty() {
		when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cart));
		when(productFeignClient.getProductById(anyLong(), anyString())).thenReturn(product);		
		when(cartItemRepository.findByIdCartAndIdproduct(anyLong(), anyLong())).thenReturn(new ArrayList<>());
		assertEquals("Product added successfully to the cart", cartService.addProductToCart(addProductRequestDto, "token").getMessage());

	}

	@Test
	void addProductToCartTest_existsCartItems_OrderNumberIsPending() {
		cartItem.setOrdernumber("Pending");
		
		when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cart));
		when(productFeignClient.getProductById(anyLong(), anyString())).thenReturn(product);		
		when(cartItemRepository.findByIdCartAndIdproduct(anyLong(), anyLong())).thenReturn(listCartItem);		
		assertEquals("Product added successfully to the cart", cartService.addProductToCart(addProductRequestDto, "token").getMessage());
	}
	
	@Test
	void addProductToCartTest_existsCartItems_OrderNumberNotPending() {		
		when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cart));
		when(productFeignClient.getProductById(anyLong(), anyString())).thenReturn(product);		
		when(cartItemRepository.findByIdCartAndIdproduct(anyLong(), anyLong())).thenReturn(listCartItem);		
		assertEquals("Product added successfully to the cart", cartService.addProductToCart(addProductRequestDto, "token").getMessage());
	}

	@Test
	void addProductToCartTest_existingCartEmpty() {
		when(cartRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());
		assertThrows(ValidationException.class, () -> {
			cartService.addProductToCart(addProductRequestDto, "token");
		});
		verify(cartItemRepository, never()).save(cartItem);
	}
	
	@Test
	void addProductToCartTest_existingProductNull() {
		when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cart));
		when(productFeignClient.getProductById(anyLong(), anyString())).thenReturn(null);
		assertThrows(ValidationException.class, () -> {
			cartService.addProductToCart(addProductRequestDto, "token");
		});
		verify(cartItemRepository, never()).save(cartItem);
	}
	
	@Test
	void addProductToCartTest_stockInsuficient() {
		addProductRequestDto.setProductquantity(12L);
		product.setStock(1L);
		when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cart));
		when(productFeignClient.getProductById(anyLong(), anyString())).thenReturn(product);
		assertThrows(ValidationException.class, () -> {
			cartService.addProductToCart(addProductRequestDto, "token");
		});
		verify(cartItemRepository, never()).save(cartItem);
	}
	
	@Test
	void increaseProductQuantityFromCart() {
		cartItem.setOrdernumber("Pending");
		when(cartItemRepository.findByIdCartAndIdproduct(anyLong(), anyLong())).thenReturn(listCartItem);
		when(productFeignClient.getProductById(anyLong(), anyString())).thenReturn(product);
		assertEquals("Quantity updated", cartService.increaseProductQuantityFromCart(1L, 1L, "token").getMessage());
	}
	
	@Test
	void decreaseProductQuantityFromCart() {
		cartItem.setOrdernumber("Pending");
		when(cartItemRepository.findByIdCartAndIdproduct(anyLong(), anyLong())).thenReturn(listCartItem);
		when(productFeignClient.getProductById(anyLong(), anyString())).thenReturn(product);
		assertEquals("Quantity updated", cartService.decreaseProductQuantityFromCart(1L, 1L, "token").getMessage());

	}

	@Test
	void updateQuantityTest_existingCartItemEmpty() {
		when(cartItemRepository.findByIdCartAndIdproduct(anyLong(), anyLong())).thenReturn(new ArrayList<>());
		assertThrows(ValidationException.class, ()->{
			cartService.increaseProductQuantityFromCart(1L, 1L, "token");
		});
		verify(cartItemRepository, never()).save(cartItem);
	}
	
	@Test
	void updateQuantityTest_existingCartItem_getOrdernumberNotPending() {
		when(cartItemRepository.findByIdCartAndIdproduct(anyLong(), anyLong())).thenReturn(listCartItem);
		assertThrows(ValidationException.class, ()->{
			cartService.increaseProductQuantityFromCart(1L, 1L, "token");
		});
		verify(cartItemRepository, never()).save(cartItem);
	}
	
	@Test
	void getallCartItemsTest() {
		when(cartItemRepository.findAllbyIdcart(anyLong())).thenReturn(listCartItem);
		assertEquals("Retrieved successfully", cartService.getallCartItems(1L, "token").getMessage());
	}
	
	@Test
	void getallCartItemsTest_itemsEmpty() {
		when(cartItemRepository.findAllbyIdcart(anyLong())).thenReturn(new ArrayList<>());
		assertThrows(ValidationException.class, ()->{
			cartService.getallCartItems(1L, "token");
		});
		verify(cartItemRepository, never()).save(cartItem);
	}
	
	@Test
	void getallItemsTest() {
		when(cartItemRepository.findAllItemsToOrder(anyLong(), anyString())).thenReturn(listCartItem);
		assertEquals("E23102013425519", cartService.getallItems(1L, "token").get(0).getOrdernumber());
	}
	
	@Test
	void getallItems_itemsEmpty() {
		when(cartItemRepository.findAllItemsToOrder(anyLong(), anyString())).thenReturn(new ArrayList<>());
		assertThrows(ValidationException.class, ()->{
			cartService.getallItems(1L, "token");
		});
		verify(cartItemRepository, never()).save(cartItem);
	}
	
	@Test
	void removeItemFromCart() {
		cartItem.setOrdernumber("Pending");
		when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cart));
		when(cartItemRepository.findByIdCartAndIdproduct(anyLong(), anyLong())).thenReturn(listCartItem);
		assertEquals("Removed item succesfully", cartService.removeItemFromCart(1L, 1L).getMessage());
	}

	@Test
	void removeItemFromCart_validateIfExistsCartFalse() {
		when(cartRepository.findById(anyLong())).thenReturn(Optional.empty());
		assertThrows(ValidationException.class, ()->{
			cartService.removeItemFromCart(1L, 1L);
		});
		verify(cartItemRepository, never()).deleteById(1L);;
	}
	
	@Test
	void removeItemFromCart_existingCartItemEmpty() {
		when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cart));
		when(cartItemRepository.findByIdCartAndIdproduct(anyLong(), anyLong())).thenReturn(new ArrayList<>());
		assertThrows(ValidationException.class, ()->{
			cartService.removeItemFromCart(1L, 1L);
		});
		verify(cartItemRepository, never()).deleteById(1L);
	}
	
	@Test
	void clearCartTest(){
		when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cart));
		when(cartItemRepository.findByIdcart(anyLong())).thenReturn(listCartItem);
		assertEquals("Clean cart succesfully", cartService.cleanCart(1L).getMessage());
	}
	
	@Test
	void clearCartTest_validateCartFalse(){
		when(cartRepository.findById(anyLong())).thenReturn(Optional.empty());
		assertThrows(ValidationException.class, ()->{
			cartService.cleanCart(1L);
		});
		verify(cartItemRepository, never()).deleteById(1L);
	
	}
		
	@Test
	void updateOrderNumber() {
		when(cartItemRepository.findAllItemsToOrder(anyLong(), anyString())).thenReturn(listCartItem);
		assertEquals("Add ordernumber successfully!", cartService.updateOrderNumber(1L, "E23102013425519"));
	}
}
