package com.agripedia.app.service;

import com.agripedia.app.shared.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService extends UserDetailsService {
	UserDto createUser(UserDto user);
}
