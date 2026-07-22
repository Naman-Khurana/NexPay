package com.project.NexPay.vault.entity;

import com.project.NexPay.comman.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "card_token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 50, nullable = false, unique = true)
    private String token;


    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "vault_card_id", nullable = false)
    private VaultCard vaultCard;

    @Column(nullable = false)
    private UUID customerId;

    @Column(nullable = false)
    private UUID merchantId;

    private LocalDateTime revokedAt;


}
