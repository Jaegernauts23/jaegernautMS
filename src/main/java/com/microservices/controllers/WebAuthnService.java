package com.microservices.controllers;

import com.microservices.models.UserPasskeyEntity;
import com.microservices.repositories.UserPasskeyRepository;
import com.microservices.repositories.UserRepository;
import com.yubico.webauthn.*;
import com.yubico.webauthn.data.*;
import com.yubico.webauthn.data.exception.Base64UrlException;
import com.yubico.webauthn.exception.RegistrationFailedException;
import jakarta.annotation.sql.DataSourceDefinitions;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;


@Service
public class WebAuthnService {
    @Autowired
    RelyingParty relyingParty;
    @Autowired
    UserPasskeyRepository userPasskeyRepository;
    // Store registration requests temporarily
    private final ConcurrentHashMap<String, PublicKeyCredentialCreationOptions> pendingRegistrations = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AssertionRequest> pendingAuthentications = new ConcurrentHashMap<>();

    public PublicKeyCredentialCreationOptions startRegistration(String username) throws Base64UrlException {
        UserIdentity userIdentity = UserIdentity.builder()
                .name(username)
                .displayName(username)
                .id(new ByteArray(username.getBytes()))
                .build();
        StartRegistrationOptions registrationOptions = StartRegistrationOptions.builder()
                .user(userIdentity)
                .build();
        PublicKeyCredentialCreationOptions request = relyingParty.startRegistration(registrationOptions);
        pendingRegistrations.put(username, request);
        return request;
    }

    public void finishRegistration(String username, PublicKeyCredential<AuthenticatorAttestationResponse, ClientRegistrationExtensionOutputs> credential)
            throws RegistrationFailedException {
        
        System.out.println("finishRegistration called for user: " + username);
        
        PublicKeyCredentialCreationOptions request = pendingRegistrations.remove(username);
        if (request == null) {
            System.out.println("ERROR: No pending registration found for user: " + username);
            throw new RegistrationFailedException(new IllegalArgumentException("No pending registration for user: " + username));
        }
        
        System.out.println("Found pending registration for user: " + username);

        try {
            FinishRegistrationOptions options = FinishRegistrationOptions.builder()
                    .request(request)
                    .response(credential)
                    .build();

            System.out.println("Calling relyingParty.finishRegistration...");
            RegistrationResult result = relyingParty.finishRegistration(options);
            System.out.println("Registration result obtained successfully");

            // Save passkey after successful registration
            UserPasskeyEntity passkey = new UserPasskeyEntity();
            passkey.setUsername(username);
            passkey.setCredentialId(result.getKeyId().getId().getBase64Url());
            passkey.setPublicKey(result.getPublicKeyCose().getBase64Url());
            passkey.setSignatureCount(result.getSignatureCount());
            
            System.out.println("Saving passkey to database for user: " + username);
            userPasskeyRepository.save(passkey);
            System.out.println("SUCCESS: Passkey saved for user: " + username);
            
        } catch (Exception e) {
            System.out.println("ERROR in finishRegistration: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public PublicKeyCredentialRequestOptions startAuthentication(String username){
        StartAssertionOptions assertionOptions=StartAssertionOptions.builder()
                .username(username)
                .build();
        AssertionRequest request = relyingParty.startAssertion(assertionOptions);
        pendingAuthentications.put(username,request);
       return request.getPublicKeyCredentialRequestOptions();

    }

    public boolean finishAuthentication(String username, PublicKeyCredential<AuthenticatorAssertionResponse, ClientAssertionExtensionOutputs> credential) {
        try {
            AssertionRequest request = pendingAuthentications.remove(username);
            if (request == null) {
                throw new RuntimeException("No pending authentication for user: " + username);
            }

            FinishAssertionOptions options = FinishAssertionOptions.builder()
                    .request(request)
                    .response(credential)
                    .build();

            AssertionResult result = relyingParty.finishAssertion(options);

            if (result.isSuccess()) {
                // Update signature count
                UserPasskeyEntity passkey = userPasskeyRepository.findByCredentialId(credential.getId().getBase64Url());
                if (passkey != null) {
                    passkey.setSignatureCount(result.getSignatureCount());
                    userPasskeyRepository.save(passkey);
                }
                System.out.println("Authentication successful for user: " + username);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println("Authentication failed: " + e.getMessage());
            return false;
        }
    }
}
