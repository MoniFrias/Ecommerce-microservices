package com.mf.orderservice.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemsResponseDTO {

	private Long idproduct;
	private String productName;
	private String sku;
	private String brandname;
	private Long productQuantity;	
	private BigDecimal pricePerUnit;
	private BigDecimal totalPrice;
}
