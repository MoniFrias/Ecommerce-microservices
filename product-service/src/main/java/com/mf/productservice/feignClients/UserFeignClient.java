package com.mf.productservice.feignClients;

import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.mf.productservice.model.User;


@FeignClient(name = "user-service", url = "http://localhost:8081/api/v1/user")
public interface UserFeignClient {

	@GetMapping("/getUser/{email}")
	public Optional<User> getUser(@PathVariable String email);
}
