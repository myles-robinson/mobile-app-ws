package com.myles.app.ws.model.response;

import lombok.Data;

@Data
public class UserRest {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
}