package com.agripedia.app.service;

import com.agripedia.app.entity.UserEntity;
import com.agripedia.app.repository.UserRepository;
import com.agripedia.app.util.RandomUserId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.agripedia.app.shared.dto.UserDto;

import javax.persistence.NonUniqueResultException;

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
	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
		return null;
	}
}
