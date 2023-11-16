package com.mf.orderservice.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.mf.orderservice.dto.response.Response;
import com.mf.orderservice.service.OrderService;

@ExtendWith(OutputCaptureExtension.class)
@WebMvcTest
class OrderControllerTest {
	
	@InjectMocks
	private OrderController orderController;

	@MockBean
	private OrderService orderService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Mock
	RuntimeException ex;
	
	private Response response;
	
	@BeforeEach
	void setup() {
		response =  new Response("Ok", new Object());
	}
	
	@Test
	void createOrderTest() throws Exception {
		when(orderService.createOrder(anyLong(), anyString())).thenReturn(response);
		ResultActions result = mockMvc.perform(post("/api/v1/order/createOrder")
				.param("idcart", "1"));
		result.andExpect(status().isCreated());
	}
	
	@Test
	void cancelOrderTest() throws Exception {
		when(orderService.cancelOrder(anyString(), anyString())).thenReturn(response);
		ResultActions result = mockMvc.perform(post("/api/v1/order/cancelOrder")
				.param("ordernumber", "E23102013425519"));
		result.andExpect(status().isOk());
	}

	@Test
	void getOrderByOrderNumberTest() throws Exception {
		when(orderService.getOrderByOrderNumber(anyString(), anyString())).thenReturn(response);
		mockMvc.perform(get("/api/v1/order/getOrderByOrderNumber/{ordernumber}", "E23102013425519"))
		.andExpect(status().isOk());
	}
	
	@Test
	void getAllordersTest() throws Exception {
		when(orderService.getAllorders(anyString())).thenReturn(response);
		mockMvc.perform(get("/api/v1/order/getAllorders"))
		.andExpect(status().isOk());
	}
	
	@Test
	void updatePaymentTest() throws Exception {
		when(orderService.updatePayment(anyString(), anyString())).thenReturn("Ok");
		
		ResultActions result = mockMvc.perform(post("/api/v1/order/updatePayment/{ordernumber}", "E23102013425519")
				.param("status", "payconfirmed"));
		
		result.andDo(print());
	}
	
	@Test
	void updateOrderTest() throws Exception {
		when(orderService.updateOrder(anyString(), anyString())).thenReturn("Ok");
		
		ResultActions result = mockMvc.perform(post("/api/v1/order/updateOrder/{ordernumber}", "E23102013425519")
				.param("status", "payconfirmed"));
		
		result.andDo(print());
	}
	
	@Test
	void fallBackOpenfeignClientsTest(final CapturedOutput capturedOutput) {
		ReflectionTestUtils.invokeMethod(orderController, "fallback", ex);
		assertThat(capturedOutput).contains("FallbackOrder is executed because open feign client is down ");
	}

}
