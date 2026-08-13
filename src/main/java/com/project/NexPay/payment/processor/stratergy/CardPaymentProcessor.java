package com.project.NexPay.payment.processor.stratergy;

import ch.qos.logback.core.testUtil.RandomUtil;
import com.project.NexPay.comman.Constants;
import com.project.NexPay.comman.util.RandomizerUtil;
import com.project.NexPay.payment.processor.PaymentProcessor;
import com.project.NexPay.payment.processor.dto.PaymentProcessorRequest;
import com.project.NexPay.payment.processor.dto.PaymentProcessorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.project.NexPay.comman.Constants.Card.CARD_PROCESSOR_;

@Component
@Slf4j
@RequiredArgsConstructor
public class CardPaymentProcessor implements PaymentProcessor {

    public static final String PAN_CARD_DECLINED = "400000000000002";
    public static final String PAN_CARD_EXPIRED = "4000000000000069";

    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {

        String pan = request.pan();

        if(PAN_CARD_DECLINED.equals(pan)){
            log.warn("Card declined");
            return new PaymentProcessorResponse.Failure(Constants.Card.CARD_DECLINED, "card declined");
        }

        if(PAN_CARD_EXPIRED.equals(pan)){
            log.warn("Pan card has expired");
            return new PaymentProcessorResponse.Failure(Constants.Card.CARD_EXPIRED, "card has expired");
        }

        String processorRef= CARD_PROCESSOR_+ RandomizerUtil.randomBase64(32);

        return new PaymentProcessorResponse.Pending(processorRef);

    }
}
