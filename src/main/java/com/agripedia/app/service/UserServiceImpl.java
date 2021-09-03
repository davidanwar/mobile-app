package com.agripedia.app.service;

import com.agripedia.app.entity.UserEntity;
import com.agripedia.app.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agripedia.app.shared.dto.UserDto;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Override
	public UserDto createUser(UserDto user) {
		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);
		userEntity.setEncryptedPassword("testPassword");
		userEntity.setUserId("testUserId");
		UserEntity storedUserDetail = userRepository.save(userEntity);
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(storedUserDetail, returnValue);
		return returnValue;
	}
	
}
