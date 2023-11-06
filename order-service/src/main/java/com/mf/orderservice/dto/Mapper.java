package com.mf.orderservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.mf.orderservice.dto.response.OrderItemsResponseDTO;
import com.mf.orderservice.dto.response.OrderResponseDTO;
import com.mf.orderservice.entity.Order;
import com.mf.orderservice.entity.StatusOrder;
import com.mf.orderservice.model.CartItem;
import com.mf.orderservice.model.Product;

@Component
public class Mapper {

	public Order mapperOrder(Long idcart, BigDecimal totalOrderPrice) {
		Order order = new Order();
		Random rand = new Random();
		DateTimeFormatter formatOrderNum = DateTimeFormatter.ofPattern("yyMMddHHmmss");
		String ordernumber = "E"+ formatOrderNum.format(LocalDateTime.now()) + rand.nextInt(100);
		order.setOrdernumber(ordernumber);
	 	order.setIdcart(idcart);
	 	order.setTotalorderprice(totalOrderPrice);
	 	order.setOrderstatus(StatusOrder.Processing);
	 	order.setPaymentstatus(StatusOrder.Pending);
	 	DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	 	order.setCreatedat(format.format(LocalDateTime.now()));
	 	order.setUpdatedat(format.format(LocalDateTime.now()));
		return order;
	}

	public OrderItemsResponseDTO orderItemsResponseDto(Product product, CartItem item) {
		OrderItemsResponseDTO orderItems = new OrderItemsResponseDTO();
		orderItems.setIdproduct(product.getIdproduct());
 		orderItems.setProductName(product.getProductname());
 		orderItems.setSku(product.getIdsku());
 		orderItems.setBrandname(product.getBrandname());
 		orderItems.setProductQuantity(item.getProductquantity());
 		orderItems.setPricePerUnit(product.getPriceperunit());
 		orderItems.setTotalPrice(item.getTotalprice());	
 		return orderItems;
		
	}

	public OrderResponseDTO mapperToOrderResponseDto(Order order, BigDecimal totalOrderPrice,
			List<OrderItemsResponseDTO> items) {
		OrderResponseDTO orderResponse = new OrderResponseDTO();
	 	orderResponse.setOrdernumber(order.getOrdernumber());
	 	orderResponse.setOrderstatus(order.getOrderstatus());
	 	orderResponse.setPaymentstatus(order.getPaymentstatus());
	 	orderResponse.setTotalorderprice(totalOrderPrice);
	 	orderResponse.setItems(items);
		return orderResponse;
	}

}
