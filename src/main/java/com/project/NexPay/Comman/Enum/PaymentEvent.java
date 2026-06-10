package com.project.NexPay.Comman.Enum;

public enum PaymentEvent {

    AUTHORIZE_ATTEMPT,
    AUTHORIZE_FAIL,
    AUTHORIZE_SUCCESS,
    CAPTURE_SUCCESS,
    CAPTURE_FAIL,
    CAPTURE_REQUEST,
    REFUND_INIT,
    REFUND_COMPLETE,
    SETTLE,
    CANCEL,
    CAPTURE_TIMEOUT

}
