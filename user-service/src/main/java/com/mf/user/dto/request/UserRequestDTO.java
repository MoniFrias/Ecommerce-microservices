package com.mf.user.dto.request;

import lombok.Data;

@Data
public class UserRequestDTO {

	private String firstname;
	private String lastname;
	private String email;
	private String password;
}
