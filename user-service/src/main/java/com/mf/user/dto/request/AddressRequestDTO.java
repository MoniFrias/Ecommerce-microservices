package com.mf.user.dto.request;

import lombok.Data;

@Data
public class AddressRequestDTO {

	private String addressline;
	private String city;
	private String zipcode;
	private String state;
	private String country;
	private String userEmail;
}
