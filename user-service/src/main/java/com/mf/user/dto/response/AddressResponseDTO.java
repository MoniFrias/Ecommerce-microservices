package com.mf.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponseDTO {

	private String addressline;
	private String city;
	private String zipcode;
	private String state;
	private String country;
}
