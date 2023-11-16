package com.mf.orderservice.service;

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
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mf.orderservice.dto.Mapper;
import com.mf.orderservice.dto.response.OrderResponseDTO;
import com.mf.orderservice.entity.Order;
import com.mf.orderservice.entity.StatusOrder;
import com.mf.orderservice.exception.ValidationException;
import com.mf.orderservice.feignClients.CartFeignClient;
import com.mf.orderservice.feignClients.ProductFeignClient;
import com.mf.orderservice.repository.OrderRepository;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

	@InjectMocks
	private OrderService orderService;
	
	@Mock
	private OrderRepository orderRepository;
	
	@Mock
	private CartFeignClient cartFeignClient;
	
	@Mock
	private ProductFeignClient productFeignClient;
	
	@Mock
	private Mapper mapper;
	
	private Order order;
	private OrderResponseDTO orderResponseDTO;
	
	@BeforeEach
	void setup() {
		order = new Order(1L, "E23102013425519", 1L, BigDecimal.TEN, StatusOrder.Pending, StatusOrder.Pending, "2023-10-20 13:42:55", "2023-10-20 13:42:55");
		orderResponseDTO = new OrderResponseDTO("", StatusOrder.Pending, StatusOrder.Pending, order.getTotalorderprice(), new ArrayList<>());
	}
	
	@Test
	void createOrderTest_existeCart() {
		when(cartFeignClient.validateIfExistsCart(anyLong())).thenReturn(true);
		when(mapper.mapperOrder(anyLong(), any())).thenReturn(order);
		assertEquals("Order created successfully", orderService.createOrder(1L, "").getMessage());
	}
	
	@Test
	void createOrderTest_NotExistCart() {
		when(cartFeignClient.validateIfExistsCart(anyLong())).thenReturn(false);
		assertThrows(ValidationException.class, () -> {
			orderService.createOrder(1L, "");
		});
		
		verify(orderRepository, never()).save(any(Order.class));
	}
 
	@Test
	void cancelOrderTest() {
		when(orderRepository.findOrderByOrdernumber(anyString())).thenReturn(order);
		assertEquals("Order was cancel successfully!", orderService.cancelOrder("E23102013425519", "").getMessage());
	}
	
	@Test
	void cancelOrderTest_orderNull() {
		when(orderRepository.findOrderByOrdernumber(anyString())).thenReturn(null);
		assertThrows(ValidationException.class, () -> {
			orderService.cancelOrder("E23102013425519", "token");
		});
		
		verify(orderRepository, never()).save(any(Order.class));
	}
	
	@Test
	void cancelOrderTest_orderStatusIsSent() {
		order.setOrderstatus(StatusOrder.Sent);
		when(orderRepository.findOrderByOrdernumber(anyString())).thenReturn(order);
		assertThrows(ValidationException.class, () -> {
			orderService.cancelOrder("E23102013425519", "token");
		});
		
		verify(orderRepository, never()).save(any(Order.class));
	}
	
	@Test
	void cancelOrderTest_orderStatusIsCancelled() {
		order.setOrderstatus(StatusOrder.Cancelled);
		when(orderRepository.findOrderByOrdernumber(anyString())).thenReturn(order);
		assertThrows(ValidationException.class, () -> {
			orderService.cancelOrder("E23102013425519", "token");
		});
		
		verify(orderRepository, never()).save(any(Order.class));
	}
	
	@Test
	void cancelOrderTest_orderStatusIsDelivered() {
		order.setOrderstatus(StatusOrder.Delivered);
		when(orderRepository.findOrderByOrdernumber(anyString())).thenReturn(order);
		assertThrows(ValidationException.class, () -> {
			orderService.cancelOrder("E23102013425519", "token");
		});
		
		verify(orderRepository, never()).save(any(Order.class));
	}

	@Test
	void getOrderByOrderNumberTest() {
		when(orderRepository.findOrderByOrdernumber(anyString())).thenReturn(order);
		when(mapper.mapperToOrderResponseDto(order, order.getTotalorderprice(), new ArrayList<>())).thenReturn(orderResponseDTO);
		assertEquals("Retrieved successfully", orderService.getOrderByOrderNumber("E23102013425519", "token").getMessage());
	}
	
	@Test
	void getOrderByOrderNumberTest_orderNull() {
		when(orderRepository.findOrderByOrdernumber(anyString())).thenReturn(null);
		assertThrows(ValidationException.class, () -> {
			orderService.getOrderByOrderNumber("E23102013425519", "token");
		});
		
		verify(orderRepository, never()).save(any(Order.class));
	}

	@Test
	void getAllordersTest() {
		List<Order> listOrders = new ArrayList<>();
		listOrders.add(order);
		when(orderRepository.findAll()).thenReturn(listOrders);
		assertEquals("Retrieved successfully", orderService.getAllorders("token").getMessage());

	}
	
	@Test
	void getAllordersTest_ordersEmpty() {
		when(orderRepository.findAll()).thenReturn(new ArrayList<>());
		assertThrows(ValidationException.class, () -> {
			orderService.getAllorders("token");
		});
		
		verify(orderRepository, never()).save(any(Order.class));
	}

	@Test
	void updatePaymentTest() {
		when(orderRepository.findOrderByOrdernumber(anyString())).thenReturn(order);
		assertEquals("Updated order, payment was comfirmed!", orderService.updatePayment("E23102013425519", "payconfirmed"));
	}
	
	@Test
	void updatePaymentTest_existingOrderNull() {
		when(orderRepository.findOrderByOrdernumber(anyString())).thenReturn(null);
		assertThrows(ValidationException.class, () -> {
			orderService.updatePayment("E23102013425519", "payconfirmed");
		});
		
		verify(orderRepository, never()).save(any(Order.class));
	}
	
	@Test
	void updatePaymentTest_statusIsPayconfirmed() {
		when(orderRepository.findOrderByOrdernumber(anyString())).thenReturn(order);
		assertThrows(ValidationException.class, () -> {
			orderService.updatePayment("E23102013425519", "pending");
		});
		
		verify(orderRepository, never()).save(any(Order.class));
	}

	@Test
	void updateOrderTest_statusSent() {
		order.setOrderstatus(StatusOrder.Confirmed);
		when(orderRepository.findOrderByOrdernumber(anyString())).thenReturn(order);
		assertEquals("Updated order status to sent!", orderService.updateOrder("E23102013425519", "Sent"));		
	}
	
	@Test
	void updateOrderTest_status_Delivered() {
		order.setOrderstatus(StatusOrder.Sent);
		when(orderRepository.findOrderByOrdernumber(anyString())).thenReturn(order);
		assertEquals("Updated order status to delivered!", orderService.updateOrder("E23102013425519", "Delivered"));	
	}
	
	@Test
	void updateOrderTest_existingOrderNull() {
		when(orderRepository.findOrderByOrdernumber(anyString())).thenReturn(null);
		assertThrows(ValidationException.class, () -> {
			orderService.updateOrder("E23102013425519", "Delivered");
		});
		
		verify(orderRepository, never()).save(any(Order.class));
	}
	
	@Test
	void updateOrderTest_statusDifferentToSentAndDelivered() {
		order.setOrderstatus(StatusOrder.Pending);
		when(orderRepository.findOrderByOrdernumber(anyString())).thenReturn(order);
		assertThrows(ValidationException.class, () -> {
			orderService.updateOrder("E23102013425519", "Delivered");
		});
		
		verify(orderRepository, never()).save(any(Order.class));
	}
}
