package com.microservices.controllers;

import com.microservices.models.OAuthClientEntity;
import com.microservices.models.OAuthClientRegistrationRequest;
import com.microservices.services.OAuthRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth/register")
@RequiredArgsConstructor
public class OAuthController{

        @Autowired
        OAuthRegistrationService oauthRegService;

 // OAuthClient Registration API's
        @PostMapping
        public ResponseEntity<OAuthClientEntity> registerClient(@RequestBody OAuthClientRegistrationRequest request) {
            OAuthClientEntity client = oauthRegService.registerClient(
                    request.getClientName(),
                    request.getRedirectUri(),
                    request.getScope()
            );
            return ResponseEntity.ok(client);
        }

        @GetMapping("/{clientId}")
        public ResponseEntity<OAuthClientEntity> getClient(@PathVariable String clientId) {
            OAuthClientEntity client = oauthRegService.getClientDetails(clientId);
            return client != null ? ResponseEntity.ok(client) : ResponseEntity.notFound().build();
        }


}