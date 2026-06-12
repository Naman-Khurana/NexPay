package com.project.NexPay.operations.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "settlement_payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SettlementPayment {

    @EmbeddedId
    private SettlementPaymentId id;

    @MapsId("settlementId")
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "settlement_id", nullable = false)
    private Settlement settlement;

}
