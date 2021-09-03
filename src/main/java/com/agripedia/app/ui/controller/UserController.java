package com.agripedia.app.ui.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agripedia.app.service.UserService;
import com.agripedia.app.shared.dto.UserDto;
import com.agripedia.app.ui.model.request.UserDetailRequest;
import com.agripedia.app.ui.model.response.UserRest;

import ch.qos.logback.core.joran.util.beans.BeanUtil;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@GetMapping
	public String getUser() {
		return "get user was called";
	}
	
	@PostMapping
	public UserRest createUser(@RequestBody UserDetailRequest userDetails) {
		UserRest returnValue = new UserRest();
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);
		UserDto createUser = userService.createUser(userDto);
		BeanUtils.copyProperties(createUser, returnValue);
		return returnValue;
	}
	
	@PutMapping
	public String updateUser() {
		return "update user was called";
	}
	
	@DeleteMapping
	public String deleteUser() {
		return "delete user was called";
	}

}
