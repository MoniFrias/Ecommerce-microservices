package com.mf.cartservice.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponseDTO {

	private Long idcartitem;
	private Long idproduct;
	private String productName;
	private Long productQuantity;
	private BigDecimal totalPrice;
	private BigDecimal pricePerUnit;
}
