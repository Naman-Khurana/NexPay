package com.project.NexPay.comman;

public class Constants {

    public static final class Nbk{
        public static final String SUCCESS="NBK_SUCCESS";
        public static final String FAILED ="NBK_FAILED";
        public static final String NBK_REF ="NBK_REF";
    }

    public static final class Upi{
        public static final String SUCCESS="UPI_SUCCESS";
        public static final String FAILED ="UPI_FAILED";
        public static final String UPI_REF ="UPI_REF";
    }

    public static final class Card{
        public static final String SUCCESS="CARD_SUCCESS";
        public static final String FAILED ="CARD_FAILED";
        public static final String CARD_DECLINED = "CARD_DECLINED";
        public static final String CARD_EXPIRED= "CARD_EXPIRED";
        public static final String CARD_REF ="CARD_REF";
        public static final String TOKEN =  "token";
        public static final String CARD_PROCESSOR_ = "CARD_PROCESSOR_";
    }

    public static final class BankSimulator{
        public static final String SIM_BANK_REF="SIM_BANK_REF";
        public static final String SIM_BANK_ERROR_CODE="SIM_BANK_ERROR_CODE";
    }

    public static final class Security{
        public static final String AUTHORIZATION_HEADER = "Authorization";
        public static final String BASIC_PREFIX = "Basic ";
        public static final String BEARER_PREFIX = "Bearer";
    }


}


