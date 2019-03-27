package com.myles.app.ws.security;

import com.myles.app.ws.SpringApplicationContext;
import org.springframework.stereotype.Component;


@Component
public class SecurityConstants {

    public static final long EXPIRATION_TIME = 864000000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users";
    public static final String H2_CONSOLE = "/h2-console/**";

    // used to read value from property value
    public static String getTokenSecret() {
        AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("AppProperties");
        return appProperties.getTokenSecret();
    }
}
