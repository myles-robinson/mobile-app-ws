package com.myles.app.ws.controller;

import com.myles.app.ws.constants.RequestMappings;
import com.myles.app.ws.dto.AddressDTO;
import com.myles.app.ws.dto.UserDTO;
import com.myles.app.ws.exceptions.FirstNameException;
import com.myles.app.ws.model.request.UserDetailsRequestModel;
import com.myles.app.ws.model.response.*;
import com.myles.app.ws.service.AddressService;
import com.myles.app.ws.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(RequestMappings.USERS) // http://localhost:8080/mobile-app-ws/users
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    AddressService addressService;

    // can consume and produce in json and xml
    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE
            , MediaType.APPLICATION_XML_VALUE})
    public UserRest getUser(@PathVariable String id) {

        UserRest returnValue = new UserRest();

        UserDTO userDTO = userService.getUserByUserId(id);
        BeanUtils.copyProperties(userDTO, returnValue);

        return returnValue;
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "limit", defaultValue = "2") int limit) {

        List<UserRest> returnValue = new ArrayList<>();

        List<UserDTO> users = userService.getUsers(page, limit);

        for (UserDTO user : users) {
            UserRest userModel = new UserRest();
            BeanUtils.copyProperties(user, userModel);
            returnValue.add(userModel);
        }

        return returnValue;
    }

    // can consume and produce in json and xml
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception{

        UserRest returnValue;

        if (userDetails.getFirstName().isEmpty()) {
            throw new FirstNameException("You are missing the first name");
        }

        // copy information from input to empty dto
        // ModelMapper creates deep copy
        ModelMapper mapper = new ModelMapper();
        UserDTO userDTO = mapper.map(userDetails, UserDTO.class);
        // do logic to create user
        UserDTO createdUser = userService.createUser(userDTO);
        // copy information from created user to return value
        returnValue = mapper.map(createdUser, UserRest.class);

        return returnValue;
    }

    @PutMapping(path = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {

        UserRest returnValue = new UserRest();

        // create new object to store data
        UserDTO userDTO = new UserDTO();
        // copy information from input to empty dto
        BeanUtils.copyProperties(userDetails, userDTO);
        // do logic to create user
        UserDTO createdUser = userService.updateUser(id, userDTO);
        // copy information from created user to return value
        BeanUtils.copyProperties(createdUser, returnValue);

        return returnValue;
    }

    @DeleteMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public OperationStatusModel deleteUser(@PathVariable String id) {

        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.DELETE.toString());

        userService.deleteUser(id);

        returnValue.setOperationResult(RequestOperationStatus.SUCCESS.toString());

        return returnValue;
    }

    // http://localhost:8080/mobile-app-ws/users/*userId*/addresses
    @GetMapping(path = "/{id}/addresses", produces = {MediaType.APPLICATION_JSON_VALUE
            , MediaType.APPLICATION_XML_VALUE})
    public List<AddressRest> getUserAddresses(@PathVariable String id) {

        List<AddressRest> returnValue = new ArrayList<>();
        ModelMapper mapper = new ModelMapper();

        List<AddressDTO> addressesDTO = addressService.getAddresses(id);

        if (addressesDTO != null && !addressesDTO.isEmpty()) {
            // Used to copy list
            Type listType = new TypeToken<List<AddressRest>>(){}.getType();
            returnValue = mapper.map(addressesDTO, listType);

        }

        return returnValue;
    }

    @GetMapping(path = "/{userId}/addresses/{addressId}", produces = {MediaType.APPLICATION_JSON_VALUE
            , MediaType.APPLICATION_XML_VALUE})
    public AddressRest getUserAddress(@PathVariable String addressId) {

        ModelMapper mapper = new ModelMapper();

        AddressDTO addressesDTO = addressService.getAddress(addressId);

        return mapper.map(addressesDTO, AddressRest.class);
    }

}