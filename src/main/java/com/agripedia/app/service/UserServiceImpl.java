package com.agripedia.app.service;

import com.agripedia.app.entity.UserEntity;
import com.agripedia.app.exception.UserServiceException;
import com.agripedia.app.repository.UserRepository;
import com.agripedia.app.ui.model.response.ErrorMessages;
import com.agripedia.app.ui.model.response.UserRest;
import com.agripedia.app.util.RandomUserId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.agripedia.app.shared.dto.UserDto;

import javax.persistence.NonUniqueResultException;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	RandomUserId randomUserId;

	@Autowired
	PasswordEncoder passwordEncoder;


	@Override
	public UserDto createUser(UserDto user) {
		UserEntity userStored =  userRepository.findByEmail(user.getEmail());
		if (userStored != null) throw new NonUniqueResultException("User Already Exist");
		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);
		String publicUserId = randomUserId.generateUserId(30);
		userEntity.setEncryptedPassword(passwordEncoder.encode(user.getPassword()));
		userEntity.setUserId(publicUserId);
		UserEntity storedUserDetail = userRepository.save(userEntity);
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(storedUserDetail, returnValue);
		return returnValue;
	}

	@Override
	public UserDto getUser(String email) {
		UserEntity user = userRepository.findByEmail(email);
		if (user == null) throw new UsernameNotFoundException(email);
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(user, returnValue);
		return returnValue;
	}

	@Override
	public UserEntity getUserByUserId(String id) {
		UserEntity user = userRepository.findByUserId(id);

		UserRest userRest = new UserRest();
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(user, userDto);
		BeanUtils.copyProperties(userDto, userRest);
		return user;
	}

	@Override
	public UserDto updateUser(UserDto userDto, String id) {
		UserDto returnValue = new UserDto();
		UserEntity user = userRepository.findByUserId(id);
		if (user == null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		UserEntity updateUser = userRepository.save(user);
		BeanUtils.copyProperties(updateUser, returnValue);
		return returnValue;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity user = userRepository.findByEmail(email);
		if (user == null) throw new UsernameNotFoundException(email);
		return new User(user.getEmail(), user.getEncryptedPassword(), new ArrayList<>());
	}
}
