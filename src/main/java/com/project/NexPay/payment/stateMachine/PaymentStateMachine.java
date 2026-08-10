package com.project.NexPay.payment.stateMachine;

import com.project.NexPay.comman.enums.PaymentEvent;
import com.project.NexPay.comman.enums.PaymentStatus;
import com.project.NexPay.comman.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PaymentStateMachine {

    private record Transition(PaymentStatus from, PaymentEvent event){}

    public static final Map<Transition,PaymentStatus> TRANSITION = Map.ofEntries(

        //created state
        Map.entry(new Transition(PaymentStatus.CREATED,PaymentEvent.AUTHORIZE_ATTEMPT),PaymentStatus.AUTHORIZING),
        Map.entry(new Transition(PaymentStatus.CREATED,PaymentEvent.CANCEL),PaymentStatus.CANCELED),

        //authorizing state
        Map.entry(new Transition(PaymentStatus.AUTHORIZING,PaymentEvent.AUTHORIZE_FAIL),PaymentStatus.FAILED),
        Map.entry(new Transition(PaymentStatus.AUTHORIZING,PaymentEvent.AUTHORIZE_SUCCESS),PaymentStatus.AUTHORIZED),
        Map.entry(new Transition(PaymentStatus.AUTHORIZING,PaymentEvent.CANCEL),PaymentStatus.CANCELED),

        //authorized state
        Map.entry(new Transition(PaymentStatus.AUTHORIZED,PaymentEvent.CAPTURE_REQUEST),PaymentStatus.CAPTURING),
        Map.entry(new Transition(PaymentStatus.AUTHORIZED,PaymentEvent.CAPTURE_TIMEOUT),PaymentStatus.AUTH_EXPIRED),

        //capturing state
        Map.entry(new Transition(PaymentStatus.CAPTURING,PaymentEvent.CAPTURE_SUCCESS),PaymentStatus.CAPTURED),
        Map.entry(new Transition(PaymentStatus.CAPTURING,PaymentEvent.CAPTURE_FAIL),PaymentStatus.AUTHORIZED),

        //captured state
        Map.entry(new Transition(PaymentStatus.CAPTURED,PaymentEvent.REFUND_INIT),PaymentStatus.PARTIALLY_REFUNDED),
        Map.entry(new Transition(PaymentStatus.CAPTURED,PaymentEvent.REFUND_COMPLETE),PaymentStatus.REFUNDED),
        Map.entry(new Transition(PaymentStatus.CAPTURED,PaymentEvent.SETTLE),PaymentStatus.SETTLED),

        //partially refunded state
        Map.entry(new Transition(PaymentStatus.PARTIALLY_REFUNDED,PaymentEvent.REFUND_COMPLETE),PaymentStatus.REFUNDED),

        //settled state
        Map.entry(new Transition(PaymentStatus.SETTLED,PaymentEvent.REFUND_INIT),PaymentStatus.PARTIALLY_REFUNDED)
    );


    public PaymentStatus transition(PaymentStatus current, PaymentEvent event){
        PaymentStatus next= TRANSITION.get(new Transition(current,event));
        if(next == null){
            throw new InvalidStateTransitionException(current.name(),event.name());
        }
        return next;
    }

}
