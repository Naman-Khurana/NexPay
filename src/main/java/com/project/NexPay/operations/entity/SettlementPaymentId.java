package com.project.NexPay.operations.entity;

import com.project.NexPay.comman.entity.BaseEntity;
import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public class SettlementPaymentId extends BaseEntity {

    private UUID paymentId;

    private UUID settlementId;
}
