package com.mf.cartservice.dto;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.mf.cartservice.dto.request.AddProductRequestDto;
import com.mf.cartservice.dto.response.CartResponseDTO;
import com.mf.cartservice.entity.Cart;
import com.mf.cartservice.entity.CartItem;
import com.mf.cartservice.model.Product;

@Component
public class Mapper {

	public CartResponseDTO cartResponseDto(Product existingProduct, CartItem newItem) {
		CartResponseDTO response = new CartResponseDTO();
		response.setIdcartitem(newItem.getIdcartitem());
		response.setIdproduct(existingProduct.getIdproduct());
		response.setProductName(existingProduct.getProductname());
		response.setProductQuantity(newItem.getProductquantity());
		response.setTotalPrice(newItem.getTotalprice());
		response.setPricePerUnit(existingProduct.getPriceperunit());
		return response;
	}

	public CartItem mapperToCartItem(AddProductRequestDto addProductRequestDTO, Product existingProduct,
			Optional<Cart> existingCart) {
		CartItem newItem = new CartItem();
		newItem.setIdproduct(addProductRequestDTO.getIdproduct());
		newItem.setProductquantity(addProductRequestDTO.getProductquantity());
		BigDecimal totalPrice = existingProduct.getPriceperunit()
				.multiply(new BigDecimal(addProductRequestDTO.getProductquantity()));
		newItem.setTotalprice(totalPrice);
		newItem.setCart(existingCart.get());
		newItem.setOrdernumber("Pending");
		return newItem;
	}

}
