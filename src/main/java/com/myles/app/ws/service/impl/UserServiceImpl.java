package com.myles.app.ws.service.impl;

import com.myles.app.ws.dto.AddressDTO;
import com.myles.app.ws.dto.UserDTO;
import com.myles.app.ws.entity.UserEntity;
import com.myles.app.ws.exceptions.UserServiceException;
import com.myles.app.ws.model.response.ErrorMessages;
import com.myles.app.ws.repository.UserRepository;
import com.myles.app.ws.service.UserService;
import com.myles.app.ws.utils.UserIdUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepo;

    @Autowired
    UserIdUtils utils;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDTO getUser(String email) {
        UserEntity userEntity = userRepo.findByEmail(email);

        if (userEntity == null) throw new UsernameNotFoundException(email);

        UserDTO returnValue = new UserDTO();
        BeanUtils.copyProperties(userEntity, returnValue);
        return returnValue;
    }

    @Override
    public List<UserDTO> getUsers(int page, int limit) {

        List<UserDTO> returnValue = new ArrayList<>();

        if (page > 0) page = page - 1;

        // Used for pagination, not return possible thousands of records at once
        Pageable pageableRequest = PageRequest.of(page, limit);
        Page<UserEntity> usersPage = userRepo.findAll(pageableRequest);

        List<UserEntity> users = usersPage.getContent();

        for (UserEntity user : users) {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            returnValue.add(userDTO);
        }

        return returnValue;
    }

    @Override
    public UserDTO getUserByUserId(String UserId) {

        UserDTO returnValue = new UserDTO();

        UserEntity userEntity = userRepo.findByUserId(UserId);

        // check if record exists
        if (userEntity == null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        BeanUtils.copyProperties(userEntity, returnValue);

        return returnValue;
    }

    @Override
    public UserDTO createUser(UserDTO user) {

        // check if user already exists
        if (userRepo.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("User with email " + user.getEmail().toUpperCase() + " already exists");
        }

        for (int i = 0; i < user.getAddresses().size(); i++) {
            AddressDTO address = user.getAddresses().get(i);
            address.setAddressId(utils.generateAddressId(30));
            // set address back to user dto
            user.getAddresses().set(i, address);
        }

        UserEntity userEntity;
//        BeanUtils.copyProperties(user, userEntity);
        ModelMapper mapper = new ModelMapper();
        userEntity = mapper.map(user, UserEntity.class);

        userEntity.setUserId(utils.generateUserId(30));
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        UserEntity storedUserDetails = userRepo.save(userEntity);

        UserDTO returnValue;

//        BeanUtils.copyProperties(storedUserDetails, returnValue);
        returnValue = mapper.map(storedUserDetails, UserDTO.class);

        return returnValue;
    }

    @Override
    public UserDTO updateUser(String userId, UserDTO user) {

        UserDTO returnValue = new UserDTO();

        // read record from DB
        UserEntity userEntity = userRepo.findByUserId(userId);

        if (userEntity == null) { // check if record exists
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }

        // update fields
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());

        UserEntity updatedDetails = userRepo.save(userEntity);

        BeanUtils.copyProperties(updatedDetails, returnValue);

        return returnValue;
    }

    @Override
    public void deleteUser(String userId) {

        UserEntity userEntity = userRepo.findByUserId(userId);

        if (userEntity == null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.toString());

        userRepo.delete(userEntity);
    }

    // load user details from db to sign in
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepo.findByEmail(email);

        if (userEntity == null) throw new UsernameNotFoundException(email);

        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }
}
