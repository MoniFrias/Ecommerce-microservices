package com.mf.orderservice.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.mf.orderservice.dto.Mapper;
import com.mf.orderservice.dto.response.OrderItemsResponseDTO;
import com.mf.orderservice.dto.response.OrderResponseDTO;
import com.mf.orderservice.entity.Order;
import com.mf.orderservice.entity.StatusOrder;
import com.mf.orderservice.exception.ValidationException;
import com.mf.orderservice.feignClients.CartFeignClient;
import com.mf.orderservice.feignClients.ProductFeignClient;
import com.mf.orderservice.model.Product;
import com.mf.orderservice.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final CartFeignClient cartFeignClient;
	private final ProductFeignClient productFeignClient;
	private final Mapper mapper;
	private static Logger logger = LogManager.getLogger(OrderService.class);

	public OrderResponseDTO createOrder(Long idcart, String token) {
		boolean existCart = cartFeignClient.validateIfExistsCart(idcart);
		BigDecimal totalOrderPrice = BigDecimal.ZERO;

		if (existCart) {
			try {
				List<OrderItemsResponseDTO> items = getItemsWithProductInformation(idcart, "Pending", token);

				for (OrderItemsResponseDTO item : items) {
					totalOrderPrice = totalOrderPrice.add(item.getTotalPrice());
					productFeignClient.updateStockOrder(item.getProductQuantity(), item.getSku(), "createOrder", token);
				}

				Order order = mapper.mapperOrder(idcart, totalOrderPrice);
				OrderResponseDTO orderResponse = mapper.mapperToOrderResponseDto(order, totalOrderPrice, items);
				orderRepository.save(order);
				cartFeignClient.updateOrderNumber(idcart, order.getOrdernumber());
				logger.info("Order created successfully");
				return orderResponse;
			} catch (Exception e) {
				throw new ValidationException("The cart is empty");
			}

		}
		throw new ValidationException("Does not exists cart with id: " + idcart);
	}

	private List<OrderItemsResponseDTO> getItemsWithProductInformation(Long idcart, String ordernumber, String token) {
		List<OrderItemsResponseDTO> items = cartFeignClient.getallItems(idcart, ordernumber).stream().map(item -> {
			try {
				Product product = productFeignClient.getProductById(item.getIdproduct(), token);
				return mapper.orderItemsResponseDto(product, item);
			} catch (Exception e) {
				throw new ValidationException("There is no product with that id");
			}
		}).collect(Collectors.toList());
		return items;
	}

	public String cancelOrder(String ordernumber, String token) {
		Order order = orderRepository.findOrderByOrdernumber(ordernumber);
		if (Objects.nonNull(order)) {
			if (!order.getOrderstatus().equals(StatusOrder.Sent)) {
				if (order.getOrderstatus().equals(StatusOrder.Cancelled)
						|| order.getOrderstatus().equals(StatusOrder.Delivered)) {
					throw new ValidationException("Order is already cancel or delivered");
				}
				order.setOrderstatus(StatusOrder.Cancelled);
				List<OrderItemsResponseDTO> items = getItemsWithProductInformation(order.getIdcart(),
						order.getOrdernumber(), token);
				for (OrderItemsResponseDTO item : items) {
					productFeignClient.updateStockOrder(item.getProductQuantity(), item.getSku(), "cancelOrder", token);
				}
				orderRepository.save(order);
				logger.info("Order successfully canceled");
				return "order was cancel successfully!";
			}
			throw new ValidationException("Cant cancel order, is already sent!");
		}
		throw new ValidationException("Does not exists order with number: " + ordernumber);
	}

	public OrderResponseDTO getOrderByOrderNumber(String ordernumber, String token) {
		Order order = orderRepository.findOrderByOrdernumber(ordernumber);
		if (Objects.nonNull(order)) {
			List<OrderItemsResponseDTO> items = getItemsWithProductInformation(order.getIdcart(), ordernumber, token);
			return mapper.mapperToOrderResponseDto(order, order.getTotalorderprice(), items);
		}
		throw new ValidationException("Does not exists order with number: " + ordernumber);
	}

	public List<OrderResponseDTO> getAllorders(String token) {
		List<OrderResponseDTO> orders = orderRepository.findAll().stream().map(order -> {
			List<OrderItemsResponseDTO> items = getItemsWithProductInformation(order.getIdcart(),
					order.getOrdernumber(), token);
			return mapper.mapperToOrderResponseDto(order, order.getTotalorderprice(), items);
		}).collect(Collectors.toList());

		if (!orders.isEmpty()) {
			return orders;
		}
		throw new ValidationException("There are not orders saved!");
	}

	public String updatePayment(String ordernumber, String status) {
		Order existingOrder = orderRepository.findOrderByOrdernumber(ordernumber);
		if (Objects.nonNull(existingOrder)) {
			if (status.equals("payconfirmed")) {
				existingOrder.setPaymentstatus(StatusOrder.PayConfirmed);
				existingOrder.setOrderstatus(StatusOrder.Confirmed);
				orderRepository.save(existingOrder);
				return "Updated order, payment was comfirmed!";
			}
			throw new ValidationException("Something wrong happened with payment");
		}
		throw new ValidationException("Order with that number does not exists");
	}

	public String updateOrder(String ordernumber, String status) {
		Order existingOrder = orderRepository.findOrderByOrdernumber(ordernumber);
		if (Objects.nonNull(existingOrder)) {

			if (existingOrder.getOrderstatus().equals(StatusOrder.Confirmed) && status.equals("Sent")) {
				existingOrder.setOrderstatus(StatusOrder.Sent);
				orderRepository.save(existingOrder);
				return "Updated order status to sent!";
			} else if (existingOrder.getOrderstatus().equals(StatusOrder.Sent) && status.equals("Delivered")) {
				existingOrder.setOrderstatus(StatusOrder.Delivered);
				orderRepository.save(existingOrder);
				return "Updated order status to delivered!";
			}
			throw new ValidationException(
					"Something wrong happened with update order, confirm if its confirmed or sent");
		}
		throw new ValidationException("Order with that number does not exists");
	}
}
