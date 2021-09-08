package com.agripedia.app.ui.controller;

import com.agripedia.app.config.SecurityConstant;
import com.agripedia.app.entity.UserEntity;
import com.agripedia.app.repository.UserRepository;
import com.agripedia.app.ui.model.response.ErrorMessages;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.agripedia.app.service.UserService;
import com.agripedia.app.shared.dto.UserDto;
import com.agripedia.app.ui.model.request.UserDetailRequest;
import com.agripedia.app.ui.model.response.UserRest;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	UserService userService;

	@Autowired
	HttpServletRequest request;

	@Autowired
	UserRepository userRepository;

	@GetMapping(path = "/{id}")
	public Optional<UserEntity> getUser(@PathVariable(value = "id") String id) {
		Optional<UserEntity> returnValue = userService.getUserByUserId(id);
		//UserDto userDto =
		//BeanUtils.copyProperties(userDto, returnValue);
		return returnValue;
	}
	
	@PostMapping
	public UserRest createUser(@RequestBody UserDetailRequest userDetails) throws Exception {
		UserRest returnValue = new UserRest();
		if (userDetails.getEmail().isEmpty()) throw new Exception(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);
		UserDto createUser = userService.createUser(userDto);
		BeanUtils.copyProperties(createUser, returnValue);
		return returnValue;
	}
	
	@PutMapping
	public String updateUser() {
		String token = request.getHeader(SecurityConstant.HEADER_STRING);
		token = token.replace(SecurityConstant.TOKEN_PREFIX, "");
		String user = Jwts.parser()
				.setSigningKey(SecurityConstant.getTokenSecret())
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
		LOG.info("TOKEN {}", token);
		LOG.info("USER {}", user);
		return "Update";
	}
	
	@DeleteMapping
	public String deleteUser() {
		return "delete user was called";
	}

}
