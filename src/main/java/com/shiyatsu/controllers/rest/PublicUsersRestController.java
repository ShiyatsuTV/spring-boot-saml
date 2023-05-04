package com.shiyatsu.controllers.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shiyatsu.basic.UserDto;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.MediaType;

@RestController
@RequestMapping(value = "/public/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class PublicUsersRestController {

	@GetMapping
	public List<UserDto> getUsers() {
		List<UserDto> users = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			users.add(new UserDto());
		}
		return users;
	}
	
}
