package com.microservices.repositories;

import com.microservices.models.UserPasskeyEntity;
import com.yubico.webauthn.CredentialRepository;
import com.yubico.webauthn.RegisteredCredential;
import com.yubico.webauthn.data.ByteArray;
import com.yubico.webauthn.data.PublicKeyCredentialDescriptor;
import com.yubico.webauthn.data.exception.Base64UrlException;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public class UserpasskeyCredentialRepository implements CredentialRepository {
    @Autowired
    UserPasskeyRepository userPasskeyRepository;
    @Override
    public Set<PublicKeyCredentialDescriptor> getCredentialIdsForUsername(String username) {
        return Set.of();
    }

    @Override
    public Optional<ByteArray> getUserHandleForUsername(String username) {
        return Optional.of(new ByteArray(username.getBytes()));
    }

    @Override
    public Optional<String> getUsernameForUserHandle(ByteArray userHandle) {
        return Optional.of(new String(userHandle.getBytes()));
    }

    @Override
    public Optional<RegisteredCredential> lookup(ByteArray credentialId, ByteArray userHandle) {
        String credId = credentialId.getBase64Url();

        UserPasskeyEntity passkey = userPasskeyRepository.findByCredentialId(credId);
        if (passkey == null) {
            return Optional.empty();
        }

        try {
            return Optional.of(RegisteredCredential.builder()
                    .credentialId(credentialId)
                    .userHandle(userHandle)
                    .publicKeyCose(ByteArray.fromBase64Url(passkey.getPublicKey()))
                    .signatureCount(passkey.getSignatureCount())
                    .build());
        } catch (Base64UrlException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<RegisteredCredential> lookupAll(ByteArray credentialId) {
        return Set.of();
    }
}
