package com.project.NexPay.operations.entity;

import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public class SettlementPaymentId {

    private UUID paymentId;

    private UUID settlementId;
}
