package com.project.NexPay.Vault.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "vault_card")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VaultCard {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private byte[] encryptedPan;

    @Column(nullable = false)
    private byte[] encryptedDek; // dek encrypts pan | and master key encrypts dek | 2 layer security

    @Column(nullable = false,length = 4)
    private String lastFour;

    @Column(nullable = false)
    private String brand;   // VISA, MASTERCARD, RUPAY

    @Column(nullable = false,length = 6)
    private String bin; // first 6 digits

    @Column(nullable = false)
    private String expiryMonth;

    @Column(nullable = false)
    private String expiryYear;

    @Column(nullable = false)
    private String cardHolderName;

    private LocalDateTime deletedAt;

}
