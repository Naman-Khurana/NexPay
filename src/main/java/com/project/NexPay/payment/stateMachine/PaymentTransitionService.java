package com.project.NexPay.payment.stateMachine;


import com.project.NexPay.comman.enums.PaymentActor;
import com.project.NexPay.comman.enums.PaymentEvent;
import com.project.NexPay.comman.enums.PaymentStatus;
import com.project.NexPay.payment.entity.Payment;
import com.project.NexPay.payment.entity.PaymentTransitionLog;
import com.project.NexPay.payment.repository.PaymentTransitionLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentTransitionService {

    private final PaymentTransitionLogRepository paymentTransitionLogRepository;
    private final PaymentStateMachine stateMachine;


    @Transactional
    public PaymentStatus apply(Payment payment, PaymentEvent event){
        PaymentStatus next= stateMachine.transition(payment.getStatus(),event);
        payment.setStatus(next);
        PaymentTransitionLog log = PaymentTransitionLog.builder()
                .payment(payment)
                .eventType(event)
                .actor(PaymentActor.SYSTEM) //TODO: get the payment actor from security context
                .fromStatus(payment.getStatus())
                .toStatus(next)
                .occurredAt(LocalDateTime.now())
                .build();


        paymentTransitionLogRepository.save(log);

        return next;


    }

}
