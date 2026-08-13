package com.project.NexPay.payment.simulator;

import com.project.NexPay.comman.enums.PaymentStatus;
import com.project.NexPay.payment.entity.Payment;
import com.project.NexPay.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class BankCallbackSimulator {

    private final PaymentRepository paymentRepository;

    @Scheduled(fixedDelayString = "${payment.simulator.poll-interval-ms:5000}")
    public void processCallbacks(){
        LocalDateTime window=LocalDateTime.now().minusSeconds(1);

        List<Payment> candidates = paymentRepository.findByStatusAndCreatedAtBefore(PaymentStatus.AUTHORIZING, window);
        if(candidates.isEmpty()) return;

        for(Payment payment : candidates){
            simulatorCallback(payment);
        }
    }


    private void simulatorCallback(Payment payment){

    }

}
