package com.project.NexPay.payment.entity;

import com.project.NexPay.comman.entity.Money;
import com.project.NexPay.comman.enums.PaymentMethod;
import com.project.NexPay.comman.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderRecord order;

    // no FK — cross-service boundary
    @Column(nullable = false)
    private UUID merchantId;

    @Column(nullable = false, length = 100)
    private String idempotencyKey;


    @Embedded
    private Money amountPaise;

    @Column(nullable = false,length = 20)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 20)
    private PaymentMethod method;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "method_details",columnDefinition = "jsonb")
    private Map<String,Object> methodDetails;

    @Column(length = 100)
    private String bankReference;

    @Column(length = 100)
    private String errorCode;

    @Column(length = 255)
    private String errorDescription;

    private LocalDateTime authorizedAt;

    private LocalDateTime capturedAt;

    private LocalDateTime failedAt;

    private LocalDateTime refundedAt;

    private LocalDateTime settledAt;





}
