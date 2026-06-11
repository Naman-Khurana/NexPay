package com.project.NexPay.merchant.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "merchant_webhook_config")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MerchantWebhookConfig {

    @Id
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "merchant_id", nullable = false)
    private Merchant merchant;

    @Column(nullable = false,length = 500)
    private String targetUrl;

    @Column(nullable = false,length = 255)
    private String eventTypeFilter;

    @Builder.Default
    @Column(nullable = false)
    private boolean enabled = true;

    @Column(length = 255)
    private String webhookSecretHash;


}
