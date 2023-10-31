package com.mf.cartservice.feignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.mf.cartservice.model.User;


@FeignClient(name = "user-service", url = "http://localhost:8081/api/v1/user")
public interface UserFeignClient {
	
	@GetMapping("/getUserById/{userid}")
	public User getUserById(@PathVariable Long userid);
}
