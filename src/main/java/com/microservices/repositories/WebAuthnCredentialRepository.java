package com.microservices.repositories;

import com.microservices.models.DAO.PasskeyCredentialDAO;
import com.yubico.webauthn.CredentialRepository;
import com.yubico.webauthn.RegisteredCredential;
import com.yubico.webauthn.data.ByteArray;
import com.yubico.webauthn.data.PublicKeyCredentialDescriptor;
import com.yubico.webauthn.data.exception.Base64UrlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class WebAuthnCredentialRepository implements CredentialRepository {
    
    @Autowired
    private PasskeyCredentialRepository passkeyCredentialRepository;
    
    @Override
    public Set<PublicKeyCredentialDescriptor> getCredentialIdsForUsername(String username) {
        try {
            return passkeyCredentialRepository.findByEmail(username).stream()
                .filter(cred -> cred.getCredentialId() != null && !cred.getCredentialId().isEmpty())
                .map(cred -> {
                    try {
                        return PublicKeyCredentialDescriptor.builder()
                            .id(ByteArray.fromBase64Url(cred.getCredentialId()))
                            .build();
                    } catch (Exception e) {
                        System.err.println("⚠️ Error parsing credential ID: " + e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        } catch (Exception e) {
            System.err.println("❌ Error in getCredentialIdsForUsername: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptySet();
        }
    }
    
    @Override
    public Optional<ByteArray> getUserHandleForUsername(String username) {
        return Optional.of(ByteArray.fromBase64(Base64.getEncoder().encodeToString(username.getBytes())));
    }
    
    @Override
    public Optional<String> getUsernameForUserHandle(ByteArray userHandle) {
        return Optional.of(new String(Base64.getDecoder().decode(userHandle.getBase64())));
    }
    
    @Override
    public Optional<RegisteredCredential> lookup(ByteArray credentialId, ByteArray userHandle) {
        PasskeyCredentialDAO cred = passkeyCredentialRepository.findByCredentialId(credentialId.getBase64Url());
        if (cred == null) return Optional.empty();

        try {
            return Optional.of(RegisteredCredential.builder()
                .credentialId(ByteArray.fromBase64Url(cred.getCredentialId()))
                .userHandle(userHandle)
                .publicKeyCose(ByteArray.fromBase64Url(cred.getPublicKey()))
                .signatureCount(cred.getSignatureCount())
                .build());
        } catch (Base64UrlException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public Set<RegisteredCredential> lookupAll(ByteArray credentialId) {
        PasskeyCredentialDAO cred = passkeyCredentialRepository.findByCredentialId(credentialId.getBase64Url());
        if (cred == null) return Collections.emptySet();
        
        ByteArray userHandle = ByteArray.fromBase64(Base64.getEncoder().encodeToString(cred.getEmail().getBytes()));

        try {
            return Set.of(RegisteredCredential.builder()
                .credentialId(ByteArray.fromBase64Url(cred.getCredentialId()))
                .userHandle(userHandle)
                .publicKeyCose(ByteArray.fromBase64Url(cred.getPublicKey()))
                .signatureCount(cred.getSignatureCount())
                .build());
        } catch (Base64UrlException e) {
            throw new RuntimeException(e);
        }
    }
}
