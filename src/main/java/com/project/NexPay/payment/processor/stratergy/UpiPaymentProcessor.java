package com.project.NexPay.payment.processor.stratergy;

import com.project.NexPay.comman.exception.ErrorCodes;
import com.project.NexPay.comman.util.RandomizerUtil;
import com.project.NexPay.payment.processor.PaymentProcessor;
import com.project.NexPay.payment.processor.dto.PaymentProcessorRequest;
import com.project.NexPay.payment.processor.dto.PaymentProcessorResponse;

public class UpiPaymentProcessor implements PaymentProcessor {
    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {

        String VPA_CODE_FAIL= "fail@okaxis";

        String bankCode = request.methodDetails() != null ? request.methodDetails().get("vpa").toString() : null ;

        //simulation
        if(VPA_CODE_FAIL.equals(bankCode)){
            return new PaymentProcessorResponse.Failure(
                    ErrorCodes.UPI_REJECTED,
                    "Ba;nk Rejected the transaction registration");
        }

        String processorRef= "UPI_PROCESSOR_"+ RandomizerUtil.randomBase64(16);

        String redirectRef="https://REDIRECT_BANK.com/" + processorRef;

        return new PaymentProcessorResponse.Success(processorRef,redirectRef);
    }
}
