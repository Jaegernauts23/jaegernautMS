package com.microservices.services.PasskeyServices;

import com.microservices.models.DAO.PasskeyCredentialDAO;
import com.microservices.repositories.PasskeyCredentialRepository;
import com.yubico.webauthn.AssertionResult;
import com.yubico.webauthn.FinishAssertionOptions;
import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.StartAssertionOptions;
import com.yubico.webauthn.AssertionRequest;
import com.yubico.webauthn.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class PasskeyLoginService {
    
    @Autowired
    private RelyingParty relyingParty;
    
    @Autowired
    private PasskeyCredentialRepository passkeyCredentialRepository;
    
    private Map<ByteArray, AssertionRequest> loginChallenges = new HashMap<>();
    
    public PublicKeyCredentialRequestOptions startAuthentication() {
        try {
            StartAssertionOptions options = StartAssertionOptions.builder()
                .userVerification(UserVerificationRequirement.REQUIRED)
                .build();
            
            AssertionRequest request = relyingParty.startAssertion(options);
            
            loginChallenges.put(request.getPublicKeyCredentialRequestOptions().getChallenge(), request);
            
            return request.getPublicKeyCredentialRequestOptions();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to start authentication", e);
        }
    }
    
    public String finishAuthentication(String credentialJson) {
        try {
            PublicKeyCredential<AuthenticatorAssertionResponse, ClientAssertionExtensionOutputs> pkc = 
                PublicKeyCredential.parseAssertionResponseJson(credentialJson);
            
            Optional<ByteArray> userHandleOpt = pkc.getResponse().getUserHandle();
            if (!userHandleOpt.isPresent()) {
                throw new RuntimeException("UserHandle is null");
            }
            
            ByteArray userHandle = userHandleOpt.get();
            String email = new String(Base64.getDecoder().decode(userHandle.getBase64()));
            
            ByteArray challenge = pkc.getResponse().getClientData().getChallenge();
            AssertionRequest request = loginChallenges.get(challenge);
            if (request == null) {
                throw new RuntimeException("Challenge not found");
            }
            
            FinishAssertionOptions options = FinishAssertionOptions.builder()
                .request(request)
                .response(pkc)
                .build();
            
            AssertionResult result = relyingParty.finishAssertion(options);
            
            if (result.isSuccess()) {
                PasskeyCredentialDAO credential = passkeyCredentialRepository
                    .findByCredentialId(result.getCredentialId().getBase64Url());
                if (credential != null) {
                    credential.setSignatureCount(result.getSignatureCount());
                    passkeyCredentialRepository.save(credential);
                }
                
                loginChallenges.remove(challenge);
                return email;
            }
            
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
