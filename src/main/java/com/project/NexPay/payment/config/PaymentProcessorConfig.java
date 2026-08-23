package com.project.NexPay.payment.config;

import com.project.NexPay.comman.enums.PaymentMethod;
import com.project.NexPay.payment.processor.PaymentProcessor;
import com.project.NexPay.payment.processor.stratergy.CardPaymentProcessor;
import com.project.NexPay.payment.processor.stratergy.NetBankingPaymentProcessor;
import com.project.NexPay.payment.processor.stratergy.UpiPaymentProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class PaymentProcessorConfig {


    @Bean
    public Map<PaymentMethod, PaymentProcessor> paymentProcessorMap(){
        return Map.of(
                PaymentMethod.CARD,new CardPaymentProcessor(),
                PaymentMethod.UPI,new UpiPaymentProcessor(),
                PaymentMethod.NETBANKING,new NetBankingPaymentProcessor()
        );
    }
}
