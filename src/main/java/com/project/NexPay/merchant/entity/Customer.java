package com.project.NexPay.merchant.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "customer" ,
        indexes ={
                @Index(name = "idx_customer_merchant_id", columnList = "merchant_id"),
                @Index(name = "idx_customer_email",columnList = "email")
        } )
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // customer can belong to multiple merchants ,therefore email is not constrained to be unique
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phoneNumber;


    @Column(length = 20)
    private String gstId;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "merchant_id",nullable = false)
    private Merchant merchant;

}
