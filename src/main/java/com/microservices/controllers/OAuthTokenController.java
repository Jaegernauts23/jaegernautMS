package com.microservices.controllers;

import com.microservices.models.OAuthToken;
import com.microservices.services.JwtTokenService;
import com.microservices.services.OAuthAuthorizationService;
import com.microservices.services.OAuthRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth2")
public class OAuthTokenController {

    private final OAuthAuthorizationService oAuthAuthorizationService;
    private final OAuthRegistrationService oAuthRegistrationService;
    private final JwtTokenService jwtTokenService;

    @PostMapping("/token")
    public ResponseEntity<?> authorizeToken(@RequestBody OAuthToken oAuthToken){

        if(!"authorization_code".equals(oAuthToken.getGrantType())){
           return ResponseEntity.badRequest().body(Map.of("error", "unsupported_grant_type"));
        }
        if(!oAuthRegistrationService.isValidClient(oAuthToken.getClientId(),oAuthToken.getClientSecret())){
            return ResponseEntity.badRequest().body(Map.of("error", "unsupported client credentials"));
        }
        String userName = oAuthAuthorizationService.validateAndConsumeToken(oAuthToken.getCode(),oAuthToken.getClientId(),oAuthToken.getRedirectUri());

        if(userName==null){
            return ResponseEntity.badRequest().body(Map.of("error", "token cant process client credentials"));
        }

        String accessToken = jwtTokenService.generateAccessToken(oAuthToken.getClientId(), userName,oAuthToken.getScope());

        return ResponseEntity.ok(Map.of(
                "access_token", accessToken,
                "token_type", "Bearer",
                "expires_in", 3600
        ));
    }
}
