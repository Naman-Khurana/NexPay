package com.project.NexPay.Payment.Entity;

import com.project.NexPay.Comman.Enum.PaymentActor;
import com.project.NexPay.Comman.Enum.PaymentEvent;
import com.project.NexPay.Comman.Enum.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payment_transition_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentTransitionLog {

    @Id
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentEvent eventType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentActor actor;

    @Enumerated(EnumType.STRING)
    @Column(name = "from_status", length = 30)
    private PaymentStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "to_status", nullable = false, length = 30)
    private PaymentStatus toStatus;

    @Column(name = "occurred_at", nullable = false)
    private LocalDateTime occurredAt;


}
