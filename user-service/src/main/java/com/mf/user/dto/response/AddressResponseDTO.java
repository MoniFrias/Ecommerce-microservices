package com.mf.user.dto.response;

import lombok.Data;

@Data
public class AddressResponseDTO {

	private String addressline;
	private String city;
	private String zipcode;
	private String state;
	private String country;
}
