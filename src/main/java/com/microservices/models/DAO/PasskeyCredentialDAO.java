package com.microservices.models.DAO;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class PasskeyCredentialDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    
    String email;
    
    @Column(unique = true)
    String credentialId;
    
    @Lob
    String publicKey;
    
    Long signatureCount;
    
    String authenticatorName;
    
    @CreatedDate
    LocalDateTime createdOn;
}
