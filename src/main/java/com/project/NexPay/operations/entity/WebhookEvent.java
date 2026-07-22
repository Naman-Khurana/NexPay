package com.project.NexPay.operations.entity;

import com.project.NexPay.comman.entity.BaseEntity;
import com.project.NexPay.comman.enums.WebhookEventStatus;
import com.project.NexPay.comman.enums.WebhookEventType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
/*
    THIS TABLE IS ONLY TO KEEP LOG/ AUDITING OF WEB HOOKS FIRED / COMPLETED / FAILED / RETRIED
 */
@Entity
@Table(name = "webhook_event")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebhookEvent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID merchantId;

    @Column(nullable = false)
    private String eventType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String,Object> payload;

    @Column(nullable = false)
    private String targetUrl;

    private String signature;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WebhookEventStatus status;

    @Column(nullable = false)
    @Builder.Default
    private Integer attempts = 0;

    private Integer lastResponseCode;

    @Column(length = 1000)
    private String lastResponseBody;

    private LocalDateTime nextRetryAt;

    private LocalDateTime lastAttemptAt;

    private LocalDateTime deliveredAt;

}
