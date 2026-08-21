package com.project.NexPay.payment.simulator;

import com.project.NexPay.comman.Constants;
import com.project.NexPay.comman.enums.ChaosMode;
import com.project.NexPay.comman.enums.PaymentStatus;
import com.project.NexPay.comman.util.RandomizerUtil;
import com.project.NexPay.payment.entity.Payment;
import com.project.NexPay.payment.repository.PaymentRepository;
import com.project.NexPay.payment.service.PaymentService;
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
    private final SimulatorConfig simulatorConfig;
    private final PaymentService paymentService;

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
        SimulatorConfig.MethodSimulatorConfig methodConfig= simulatorConfig.configFor(payment.getMethod());

        LocalDateTime dueAt= dueAt(payment,methodConfig);

        if(dueAt.isAfter(LocalDateTime.now()))
            return;

        ChaosMode chaosMode = simulatorConfig.getChaosMode();

        switch (chaosMode){
            case SUCCESS -> resolve(payment, true);
            case FAILURE -> resolve(payment, false);
            case TIMEOUT -> {
                log.info("BankClassBack Simulator: Payment Timed out");
            }
            case NORMAL , SLOW->resolve(payment,shouldApprove(payment,methodConfig));
        }

    }

    private void resolve(Payment payment, boolean approve){
        if(approve){
            String bankRef= Constants.BankSimulator.SIM_BANK_REF + RandomizerUtil.randomBase64(8);
            paymentService.resolveAuthorize(payment.getId(), true, bankRef, null, null);
        }else{
            paymentService.resolveAuthorize(payment.getId(), false, null, Constants.BankSimulator.SIM_BANK_ERROR_CODE, "Simulated Bank Declined");
        }

    }

    private boolean shouldApprove(Payment payment, SimulatorConfig.MethodSimulatorConfig methodSimulatorConfig){
        int bucket = Math.abs(payment.getId().hashCode()) % 100;
        return bucket < methodSimulatorConfig.getSuccessRate();
    }

    private LocalDateTime dueAt(Payment payment, SimulatorConfig.MethodSimulatorConfig methodSimulatorConfig){
        int range = methodSimulatorConfig.getMaxDelaySeconds() - methodSimulatorConfig.getMinDelaySeconds();

        int delaySeconds= methodSimulatorConfig.getMinDelaySeconds() + (Math.abs(payment.getId().hashCode())) % (range + 1);

        delaySeconds = (simulatorConfig.getChaosMode() == ChaosMode.SLOW ) ? delaySeconds * 2 : delaySeconds;

        return payment.getCreatedAt().plusSeconds(delaySeconds);
    }


}
