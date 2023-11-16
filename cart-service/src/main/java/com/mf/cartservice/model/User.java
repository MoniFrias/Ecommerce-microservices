package com.mf.cartservice.model;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

	private Long iduser;
	private String firstname;
	private String lastname;
	private String email;
	private String password;
	@Enumerated(EnumType.STRING)
	private Role role;
}
