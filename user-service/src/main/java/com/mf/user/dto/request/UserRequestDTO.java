package com.mf.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserRequestDTO {

	private String firstname;
	private String lastname;
	private String email;
	private String password;
}
