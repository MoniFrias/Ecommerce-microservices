package com.mf.cartservice.dto.request;

import lombok.Data;

@Data
public class AddProductRequestDto {
	
	private Long idcart;
	private Long idproduct;
	private Long productquantity;	

}
