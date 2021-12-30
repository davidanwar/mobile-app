package com.agripedia.app.ui.controller;

import com.agripedia.app.config.SecurityConstant;
import com.agripedia.app.entity.UserEntity;
import com.agripedia.app.exception.UserServiceException;
import com.agripedia.app.repository.UserRepository;
import com.agripedia.app.service.AddressService;
import com.agripedia.app.shared.dto.AddressDto;
import com.agripedia.app.ui.model.request.RequestOperationName;
import com.agripedia.app.ui.model.request.RequestOperationResult;
import com.agripedia.app.ui.model.response.AddressRest;
import com.agripedia.app.ui.model.response.ErrorMessages;
import com.agripedia.app.ui.model.response.OperationStatusModel;
import io.jsonwebtoken.Jwts;
import net.bytebuddy.description.method.MethodDescription;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.*;

import com.agripedia.app.service.UserService;
import com.agripedia.app.shared.dto.UserDto;
import com.agripedia.app.ui.model.request.UserDetailRequest;
import com.agripedia.app.ui.model.response.UserRest;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	UserService userService;

	@Autowired
	AddressService addressService;

	@Autowired
	HttpServletRequest request;

	@Autowired
	UserRepository userRepository;

	@GetMapping(path = "/{id}")
	public UserRest getUser(@PathVariable(value = "id") String id) {
		UserEntity returnValue = userService.getUserByUserId(id);
		ModelMapper mapper = new ModelMapper();
		UserRest userRest = mapper.map(returnValue, UserRest.class);
		return userRest;
	}

	@GetMapping(path = "/{id}/address")
	public List<AddressRest> getListAddress(@PathVariable(value = "id") String id) {
		List<AddressRest> returnValue = new ArrayList<>();
		List<AddressDto> addressDto = addressService.getAddresses(id);
		ModelMapper mapper = new ModelMapper();
		if (addressDto != null || !addressDto.isEmpty()) {
			Type listType = new TypeToken<List<AddressRest>>() {}.getType();
			returnValue = mapper.map(addressDto, listType);
		}
		return returnValue;
	}

	@GetMapping(path = "/{userId}/address/{addressId}")
	public EntityModel<AddressRest> getAddress(@PathVariable(value = "userId") String userId, @PathVariable(value = "addressId") String addressId) {
		AddressDto addressDto = addressService.getAddress(addressId);
		ModelMapper mapper = new ModelMapper();
		AddressRest returnValue = mapper.map(addressDto, AddressRest.class);
		Link userLink =  WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUser(userId))
				//.slash(userId)
				.withRel("user");
		Link addressLink = WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(UserController.class).getListAddress(userId))
				//.slash(userId).slash("address")
				.withRel("address");
		Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getAddress(userId, addressId))
				//.slash(userId).slash("address").slash(addressId)
				.withSelfRel();
//		returnValue.add(userLink);
//		returnValue.add(addressLink);
//		returnValue.add(selfLink);

		return EntityModel.of(returnValue, Arrays.asList(userLink, addressLink, selfLink));
	}
	
	@PostMapping
	public UserRest createUser(@RequestBody UserDetailRequest userDetails) throws Exception {
		if (userDetails.getEmail().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		//UserDto userDto = new UserDto();
		//BeanUtils.copyProperties(userDetails, userDto);
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);
		UserDto createUser = userService.createUser(userDto);
		UserRest returnValue = modelMapper.map(createUser, UserRest.class);
		return returnValue;
	}
	
	@GetMapping("/token")
	public String getToken() {
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

	@PutMapping("/{id}")
	public UserRest updateUser(@RequestBody UserDetailRequest userDetails, @PathVariable(value = "id") String id) {
		UserRest returnValue = new UserRest();
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);
		UserDto updatedUser = userService.updateUser(userDto, id);
		BeanUtils.copyProperties(updatedUser, returnValue);
		return returnValue;
	}

	@GetMapping("/email-verification")
	public OperationStatusModel verifyEmailToken(@RequestParam(value = "token") String token) {
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.VERIFY_EMAIL.name());
		boolean isVerify = userService.verifyEmailToken(token);
		if (isVerify) {
			returnValue.setOperationResult(RequestOperationResult.SUCCESS.name());
		} else {
			returnValue.setOperationResult(RequestOperationResult.ERROR.name());
		}
		return returnValue;
	}

}
