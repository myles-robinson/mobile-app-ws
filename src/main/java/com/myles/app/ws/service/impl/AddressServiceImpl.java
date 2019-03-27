package com.myles.app.ws.service.impl;

import com.myles.app.ws.dto.AddressDTO;
import com.myles.app.ws.entity.AddressEntity;
import com.myles.app.ws.entity.UserEntity;
import com.myles.app.ws.repository.AddressRepository;
import com.myles.app.ws.repository.UserRepository;
import com.myles.app.ws.service.AddressService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public List<AddressDTO> getAddresses(String userId) {

        List<AddressDTO> returnValue = new ArrayList<>();
        ModelMapper mapper = new ModelMapper();

        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null) return  returnValue;

        List<AddressEntity> addresses = addressRepository.findAllByUserDetails(userEntity);

        for (AddressEntity addressEntity : addresses) {
            returnValue.add(mapper.map(addressEntity, AddressDTO.class));
        }

        return returnValue;
    }

    @Override
    public AddressDTO getAddress(String addressId) {
        ModelMapper mapper = new ModelMapper();

        return mapper.map(addressRepository.findByAddressId(addressId), AddressDTO.class);
    }
}