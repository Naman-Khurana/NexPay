package com.project.NexPay.payment.config;

import com.project.NexPay.comman.enums.PaymentMethod;
import com.project.NexPay.payment.processor.PaymentProcessor;
import com.project.NexPay.payment.processor.stratergy.CardPaymentProcessor;
import com.project.NexPay.payment.processor.stratergy.NetBankingPaymentProcessor;
import com.project.NexPay.payment.processor.stratergy.UpiPaymentProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class PaymentProcessorConfig {

    private final CardPaymentProcessor cardPaymentProcessor;
    private final NetBankingPaymentProcessor netBankingPaymentProcessor;
    private final UpiPaymentProcessor upiPaymentProcessor;



    @Bean
    public Map<PaymentMethod, PaymentProcessor> paymentProcessorMap(){
        return Map.of(
                PaymentMethod.CARD,cardPaymentProcessor,
                PaymentMethod.UPI,upiPaymentProcessor,
                PaymentMethod.NETBANKING,netBankingPaymentProcessor
        );
    }
}
