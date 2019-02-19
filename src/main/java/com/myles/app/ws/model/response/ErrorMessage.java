package com.myles.app.ws.model.response;

import lombok.Data;

import java.util.Date;

@Data
public class ErrorMessage { // used to create custom error message content
    private Date timestamp;
    private String message;

    public ErrorMessage(Date timestamp, String message) {
        this.timestamp = timestamp;
        this.message = message;
    }

    public ErrorMessage() {
    }
}
