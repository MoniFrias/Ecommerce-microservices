package com.mf.cartservice.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddProductRequestDto {
	
	private Long idcart;
	private Long idproduct;
	private Long productquantity;	

}
