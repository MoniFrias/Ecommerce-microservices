package com.mf.orderservice.dto.response;

import java.math.BigDecimal;
import java.util.List;

import com.mf.orderservice.entity.StatusOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {

	private String message;
	private String ordernumber;
	private StatusOrder orderstatus;
	private StatusOrder paymentstatus;
	private BigDecimal totalorderprice;
	private List<OrderItemsResponseDTO> items;
}
