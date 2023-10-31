package com.mf.orderservice.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
	
	private Long idcartitem;
	private Long idproduct;
	private Long productquantity;
	private BigDecimal totalprice;
	private Cart cart;

}
