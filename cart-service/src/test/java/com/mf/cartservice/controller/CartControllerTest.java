package com.mf.cartservice.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mf.cartservice.dto.request.AddProductRequestDto;
import com.mf.cartservice.dto.response.Response;
import com.mf.cartservice.entity.Cart;
import com.mf.cartservice.entity.CartItem;
import com.mf.cartservice.service.CartService;

@ExtendWith(OutputCaptureExtension.class)
@WebMvcTest
class CartControllerTest {
	
	@InjectMocks
	private CartController cartController;
	
	@MockBean
	private CartService cartService;
	
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Mock
	RuntimeException ex;

	private Response response;
	private AddProductRequestDto addProductRequestDto;
	
	@BeforeEach
	void setup() {
		response =  new Response("Ok", new Cart(1L, 1L, null));
		addProductRequestDto = AddProductRequestDto.builder()
				.idcart(1L).idproduct(1L).productquantity(1L).build();
	}
	
	@Test
	void validateIfExistsCartTest() throws Exception {
		when(cartService.validateIfExistsCart(anyLong())).thenReturn(true);
		
		mockMvc.perform(get("/api/v1/cart/validateIfExistsCart/{idcart}", 1L)).andDo(print());
	}

	@Test
	void createCartTest() throws Exception {
		when(cartService.createCart(anyLong())).thenReturn(response);
		mockMvc.perform(post("/api/v1/cart/createCart").param("iduser", "1"))
		.andDo(print()).andExpect(status().isCreated())
		.andExpect(jsonPath("$.message", is(response.getMessage())));
	}
	
	@Test
	void addProductToCartTest() throws Exception {
		when(cartService.addProductToCart(any(AddProductRequestDto.class), anyString())).thenReturn(response);
		mockMvc.perform(post("/api/v1/cart/addProductToCart")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(addProductRequestDto)))
		.andDo(print()).andExpect(status().isCreated());
	}
	
	@Test
	void increaseProductQuantityFromCartTest() throws Exception {
		LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("idcart", "1");
		params.add("idproduct", "1");
		when(cartService.increaseProductQuantityFromCart(anyLong(), anyLong(), anyString())).thenReturn(response);
		mockMvc.perform(post("/api/v1/cart/increaseQuantity")
				.params(params))
		.andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	void decreaseProductQuantityFromCartTest() throws Exception {
		LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("idcart", "1");
		params.add("idproduct", "1");
		when(cartService.decreaseProductQuantityFromCart(anyLong(), anyLong(), anyString())).thenReturn(response);
		mockMvc.perform(post("/api/v1/cart/decreaseQuantity")
				.params(params))
		.andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	void getAllCartItemsTest() throws Exception {
		when(cartService.getallCartItems(anyLong(), anyString())).thenReturn(response);
		mockMvc.perform(get("/api/v1/cart/getAllCartItems")
				.param("idcart", "1"))
		.andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	void getallItemsTest() throws Exception {
		List<CartItem> listCartItem = null;
		LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("idcart", "1");
		params.add("ordernumber", "E23102013425519");
		when(cartService.getallItems(anyLong(), anyString())).thenReturn(listCartItem);
		mockMvc.perform(get("/api/v1/cart/getallItems")
				.params(params)).andDo(print());
	}
	
	@Test
	void removeItemFromCartTest() throws Exception {
		LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("idcart", "1");
		params.add("idproduct", "1");
		
		when(cartService.removeItemFromCart(anyLong(), anyLong())).thenReturn(response);
		mockMvc.perform(delete("/api/v1/cart/removeItem")
				.params(params)).andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	void updateOrderNumberTest() throws Exception {		
		when(cartService.updateOrderNumber(anyLong(), anyString())).thenReturn("Ok");
		mockMvc.perform(put("/api/v1/cart/updateOrderNumber/{idCart}/{ordernumber}", 1L, 1L))
		.andDo(print());
	}
	
	@Test
	void cleanCartTest() throws Exception {		
		when(cartService.cleanCart(anyLong())).thenReturn(response);
		mockMvc.perform(delete("/api/v1/cart/cleanCart")
				.param("idcart", "1")).andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	void fallBackOpenfeignClientsTest(final CapturedOutput capturedOutput) {
		ReflectionTestUtils.invokeMethod(cartController, "fallBackOpenfeignClients", ex);
		assertThat(capturedOutput).contains("FallbackCart is executed because open feign client is down ");
	}
}
