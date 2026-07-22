package com.project.NexPay.operations.entity;

import com.project.NexPay.comman.entity.BaseEntity;
import com.project.NexPay.comman.entity.Money;
import com.project.NexPay.comman.enums.SettlementStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "settlement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Settlement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID merchantId;

    @AttributeOverrides({
            @AttributeOverride(name = "currency" , column = @Column(name = "gross_amount_currency",nullable = false)),
            @AttributeOverride(name = "amountUnits", column = @Column(name = "gross_amount_units",  nullable = false))
    })
    @Embedded
    private Money grossAmount;

    @AttributeOverrides({
            @AttributeOverride(name = "currency" , column = @Column(name = "refund_amount_currency",nullable = false)),
            @AttributeOverride(name = "amountUnits", column = @Column(name = "refund_amount_units",  nullable = false))
    })
    @Embedded
    private Money refundAmount;

    @AttributeOverrides({
            @AttributeOverride(name = "currency" , column = @Column(name = "fee_amount_currency",nullable = false)),
            @AttributeOverride(name = "amountUnits", column = @Column(name = "fee_amount_units",  nullable = false))
    })
    @Embedded
    private Money feeAmount;

    @AttributeOverrides({
            @AttributeOverride(name = "currency" , column = @Column(name = "gst_amount_currency",nullable = false)),
            @AttributeOverride(name = "amountUnits", column = @Column(name = "gst_amount_units",  nullable = false))
    })
    @Embedded
    private Money gstAmount;

    @AttributeOverrides({
            @AttributeOverride(name = "currency" , column = @Column(name = "net_amount_currency",nullable = false)),
            @AttributeOverride(name = "amountUnits", column = @Column(name = "net_amount_units",  nullable = false))
    })
    @Embedded
    private Money netAmount; // netAmount = grossAmount - refundAmount - feeAmount- gstAmount

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SettlementStatus status;

    private String bankReference;




}
