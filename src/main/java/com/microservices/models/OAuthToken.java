package com.microservices.models;

import lombok.Data;

@Data
public class OAuthToken {
    String grantType;
    String clientId;
    String clientSecret;
    String code;
    String scope;
    String redirectUri;

}
