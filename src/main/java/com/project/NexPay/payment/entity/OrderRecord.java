package com.project.NexPay.payment.entity;

import com.project.NexPay.comman.entity.BaseEntity;
import com.project.NexPay.comman.entity.Money;
import com.project.NexPay.comman.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "order_record",
        indexes = {
                @Index(name = "Idx_order_id_merchant_id", columnList = "id, merchant_id"),
                @Index(name = "idx_order_merchant_id", columnList = "merchant_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRecord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // no FK — cross-service boundary
    @Column(name = "merchant_id", nullable = false)
    private UUID merchantId;

    @Column(length = 100)
    private String receipt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private OrderStatus orderStatus = OrderStatus.CREATED;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> notes;

    @Embedded
    private Money amount;

    @Column(nullable = false)
    @Builder.Default
    private Integer attempts = 0;

    private LocalDateTime expiresAt;


}
