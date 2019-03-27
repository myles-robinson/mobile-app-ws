package com.myles.app.ws.service.impl;

import com.myles.app.ws.dto.AddressDTO;
import com.myles.app.ws.dto.UserDTO;
import com.myles.app.ws.entity.UserEntity;
import com.myles.app.ws.exceptions.UserServiceException;
import com.myles.app.ws.repository.UserRepository;
import com.myles.app.ws.utils.UserIdUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    UserIdUtils utils;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks // autowires and injects other mock objects required from service
    UserServiceImpl userService;

    UserEntity userEntity;


    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setFirstName("Myles");
        userEntity.setUserId("asdfasdf");
        userEntity.setEncryptedPassword("#aodn39laksd");
        userEntity.setEmail("test1@test.com");
    }

    @Test
    void testGetUser() {

        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

        UserDTO userDTO = userService.getUser("test@test.com");

        Assert.assertNotNull(userDTO);
        Assert.assertEquals("Myles", userDTO.getFirstName());
    }

    @Test
    void testGetUser_UsernameNotFoundException() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        // when user service is called, exception should be thrown
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> userService.getUser("test@test.com"));
    }

    @Test
    void testCreateUser() {

        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(utils.generateAddressId(anyInt())).thenReturn("abcdef234");
        when(utils.generateUserId(anyInt())).thenReturn("user1");
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("abc123");
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        List<AddressDTO> addressList = new ArrayList<>();
        AddressDTO addressDTO = new AddressDTO();

        addressDTO.setType("Shipping");
        addressList.add(addressDTO);

        UserDTO userDTO = new UserDTO();
        userDTO.setAddresses(addressList);

        UserDTO storedUserDetails = userService.createUser(userDTO);

        Assert.assertNotNull(storedUserDetails);
        Assert.assertEquals(userEntity.getFirstName(), storedUserDetails.getFirstName());
    }

    @Test
    void testCreateUser_UserServiceException() {

        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("Sam");
        userDTO.setLastName("Smith");
        userDTO.setEmail("test@test.com");

        Assertions.assertThrows(UserServiceException.class,
                () -> userService.createUser(userDTO));
    }
}