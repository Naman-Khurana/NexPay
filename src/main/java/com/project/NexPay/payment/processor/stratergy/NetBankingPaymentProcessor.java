package com.project.NexPay.payment.processor.stratergy;

import com.project.NexPay.comman.exception.ErrorCodes;
import com.project.NexPay.comman.util.RandomizerUtil;
import com.project.NexPay.payment.processor.PaymentProcessor;
import com.project.NexPay.payment.processor.dto.PaymentProcessorRequest;
import com.project.NexPay.payment.processor.dto.PaymentProcessorResponse;
import org.springframework.stereotype.Component;

@Component
public class NetBankingPaymentProcessor implements PaymentProcessor {
    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {

        String BANK_CODE_FAIL= "BANK_CODE_FAIL";

        String bankCode = request.methodDetails() != null ? request.methodDetails().get("bank").toString() : null ;

        //simulation
        if(BANK_CODE_FAIL.equals(bankCode)){
            return new PaymentProcessorResponse.Failure(
                    ErrorCodes.BANK_REJECTED,
                    "Ba;nk Rejected the transaction registration");
        }

        String processorRef= "NBK_PROCESSOR_"+ RandomizerUtil.randomBase64(16);

        String redirectRef="https://REDIRECT_BANK.com/" + processorRef;

        return new PaymentProcessorResponse.Pending(processorRef);
    }
}
