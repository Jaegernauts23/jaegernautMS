package com.microservices.services.PasskeyServices;

import com.microservices.models.DAO.PasskeyCredentialDAO;
import com.microservices.repositories.PasskeyCredentialRepository;
import com.microservices.repositories.UserDetailsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yubico.webauthn.FinishRegistrationOptions;
import com.yubico.webauthn.RegistrationResult;
import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.StartRegistrationOptions;
import com.yubico.webauthn.data.*;
import com.yubico.webauthn.exception.RegistrationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class PasskeyRegistrationService {
    
    @Autowired
    private RelyingParty relyingParty;
    
    @Autowired
    private PasskeyCredentialRepository passkeyCredentialRepository;
    
    @Autowired
    private UserDetailsRepository userDetailsRepository;
    
    private Map<String, PublicKeyCredentialCreationOptions> registrationChallenges = new HashMap<>();
    
    public PublicKeyCredentialCreationOptions startRegistration(String email) {
        System.out.println("Starting registration for: " + email);
        
        if (userDetailsRepository.findByEmail(email) == null) {
            System.out.println("User not found");
            return null;
        }
        
        try {
            ByteArray userHandle = ByteArray.fromBase64(Base64.getEncoder().encodeToString(email.getBytes()));
            
            StartRegistrationOptions options = StartRegistrationOptions.builder()
                .user(UserIdentity.builder()
                    .name(email)
                    .displayName(email)
                    .id(userHandle)
                    .build())
                .authenticatorSelection(AuthenticatorSelectionCriteria.builder()
                    .residentKey(ResidentKeyRequirement.REQUIRED)
                    .userVerification(UserVerificationRequirement.REQUIRED)
                    .build())
                .build();
            
            System.out.println("Calling relyingParty.startRegistration...");
            PublicKeyCredentialCreationOptions request = relyingParty.startRegistration(options);
            System.out.println("Challenge generated successfully");
            
            registrationChallenges.put(email, request);
            return request;
        } catch (Exception e) {
            System.err.println("ERROR in startRegistration: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to start registration", e);
        }
    }
    
    public boolean finishRegistration(String email, String credentialJson, String authenticatorName) {
        try {
            System.out.println("Received credential JSON: " + credentialJson);
            
            PublicKeyCredentialCreationOptions request = registrationChallenges.get(email);
            if (request == null) return false;
            
            // Parse credential JSON from browser
            ObjectMapper mapper = new ObjectMapper();
            PublicKeyCredential<AuthenticatorAttestationResponse, ClientRegistrationExtensionOutputs> pkc = 
                PublicKeyCredential.parseRegistrationResponseJson(credentialJson);
            
            // Verify credential using Yubico library
            FinishRegistrationOptions options = FinishRegistrationOptions.builder()
                .request(request)
                .response(pkc)
                .build();
            
            RegistrationResult result = relyingParty.finishRegistration(options);
            
            // Save verified credential to database
            PasskeyCredentialDAO credential = new PasskeyCredentialDAO();
            credential.setEmail(email);
            credential.setCredentialId(result.getKeyId().getId().getBase64Url());
            credential.setPublicKey(result.getPublicKeyCose().getBase64Url());
            credential.setSignatureCount(result.getSignatureCount());
            credential.setAuthenticatorName(authenticatorName != null ? authenticatorName : "Default");
            
            passkeyCredentialRepository.save(credential);
            registrationChallenges.remove(email);
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
