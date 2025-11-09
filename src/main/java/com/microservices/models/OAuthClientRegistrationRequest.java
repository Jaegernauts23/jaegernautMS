package com.microservices.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OAuthClientRegistrationRequest {
    String clientName;
    String scope;
    String redirectUri;
}
