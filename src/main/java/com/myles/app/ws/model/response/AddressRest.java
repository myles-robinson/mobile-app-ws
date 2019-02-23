package com.myles.app.ws.model.response;

import lombok.Data;

@Data
public class AddressRest {
    private String addressId;
    private String city;
    private String country;
    private String streetName;
    private String postalCode;
    private String type;
}
