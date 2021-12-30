package com.agripedia.app;

import com.agripedia.app.entity.UserEntity;
import com.agripedia.app.repository.UserRepository;
import com.agripedia.app.service.UserServiceImpl;
import com.agripedia.app.shared.dto.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class UserService {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        //MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUser() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setFirstName("David");
        user.setUserId("guiyqg82");
        user.setEncryptedPassword("8374dsbf");
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(user);
        UserDto userDto = userService.getUser("david@gmail.com");
        Assertions.assertNotNull(userDto);
        Assertions.assertEquals("David", userDto.getFirstName());
    }
}
