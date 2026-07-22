package com.project.NexPay.merchant.entity;

import com.project.NexPay.comman.entity.BaseEntity;
import com.project.NexPay.comman.enums.BusinessType;
import com.project.NexPay.comman.enums.MerchantStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "merchant",
        indexes ={
            @Index(name = "idx_merchant_status", columnList = "status")
        } )
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Merchant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 100,nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(length = 50,nullable = false)
    private BusinessType businessType;

    @Column(length = 100,nullable = false)
    private String businessName;

    @Column(length = 100,unique = true,nullable = false)
    private String email;

    @Column(length = 20,nullable = false)
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private MerchantStatus status=MerchantStatus.KYC_PENDING;

    @Column(length = 20)
    private String gstId;

    @Column(length = 20)
    private String panId;

    @Column(length = 200)
    private String settlementBankAccount;

    @Column(length = 20)
    private String settlementBankIfsc;

    @Column(length = 200)
    private String settlementBankAccountHolderName;

}
