package com.project.NexPay.comman.enums;

public enum CardBrand {
    VISA,
    MASTERCARD,
    RUPAY,
    AMEX;

    public static CardBrand detectBrand(String pan){
        if(pan.startsWith("4")) return CardBrand.VISA;
        if(pan.startsWith("5") || pan.startsWith("2") ) return CardBrand.MASTERCARD;
        if(pan.startsWith("37") || pan.startsWith("34")) return CardBrand.AMEX;
        return RUPAY;
    }
}
