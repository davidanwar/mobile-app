package com.agripedia.app.service;

import com.agripedia.app.entity.UserEntity;
import com.agripedia.app.shared.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;


public interface UserService extends UserDetailsService {
	UserDto createUser(UserDto user);
	UserDto getUser(String email);
    Optional<UserEntity> getUserByUserId(String id);
}
