package com.project.NexPay.payment.entity;

import com.project.NexPay.comman.entity.Money;
import com.project.NexPay.comman.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "order_record")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // no FK — cross-service boundary
    @Column(name = "merchant_id", nullable = false)
    private UUID merchantId;

//    private String idempotencyKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private OrderStatus orderStatus = OrderStatus.CREATED;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String,Object> notes;

    @Embedded
    private Money amountPaise;

    @Column(nullable=false)
    @Builder.Default
    private Integer attempts = 0;

}
