package com.mf.orderservice.model;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

	private Long idcart;
	private Long iduser;
	private Set<CartItem> cartItems;
}
