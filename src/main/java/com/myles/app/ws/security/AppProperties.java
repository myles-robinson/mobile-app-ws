package com.myles.app.ws.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AppProperties { // used to read values from property file

    @Autowired
    private Environment environment;

    public String getTokenSecret() {
        return environment.getProperty("tokenSecret");
    }
}