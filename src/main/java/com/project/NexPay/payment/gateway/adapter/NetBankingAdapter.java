package com.project.NexPay.payment.gateway.adapter;

import com.project.NexPay.comman.enums.PaymentMethod;
import com.project.NexPay.comman.exception.ErrorCodes;
import com.project.NexPay.payment.gateway.PaymentAdapter;
import com.project.NexPay.payment.gateway.dto.PaymentRequest;
import com.project.NexPay.payment.gateway.dto.PaymentResult;
import com.project.NexPay.payment.processor.PaymentProcessorRouter;
import com.project.NexPay.payment.processor.dto.PaymentProcessorRequest;
import com.project.NexPay.payment.processor.dto.PaymentProcessorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NetBankingAdapter implements PaymentAdapter {

    private final PaymentProcessorRouter paymentProcessorRouter;

    @Override
    public PaymentResult initiate(PaymentRequest request) {

        log.info("Initiate Payment with NetBankingAdapter, paymentId: {}",request.paymentId());

        try {
            PaymentProcessorRequest paymentProcessorRequest= PaymentProcessorRequest.nonCard(
                    request.paymentId(),
                    request.amount(),
                    PaymentMethod.NETBANKING,
                    request.methodDetails()
            );

            PaymentProcessorResponse paymentProcessorResponse = paymentProcessorRouter.charge(paymentProcessorRequest);

            return switch (paymentProcessorResponse){
                case PaymentProcessorResponse.Failure failure -> new PaymentResult.Failure(failure.error(),failure.errorDescription());

                case PaymentProcessorResponse.Pending pending-> new PaymentResult.Pending(pending.processorReference());

                case PaymentProcessorResponse.Success success -> new PaymentResult.Success(success.bankReference());
            };
        } catch (Exception e) {
            return new PaymentResult.Failure(ErrorCodes.NBK_FAILED,e.getMessage());
        }


    }
}
