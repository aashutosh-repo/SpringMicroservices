package com.bancs.payments.constants;

public class TransactionConstants {
    public static final int CASH_TRANSACTION = 1;
    public static final int TRANSFER = 2;
    public static final int NEFT = 3;
    public static final int UPI = 4;

    public static class TransactionType{
        public static final String FUND_TRANSFER = "FUND_TRANSFER";
        public static final String BILL_PAYMENT = "BILL_PAYMENT";
        public static final String MOBILE_RECHARGE = "MOBILE_RECHARGE";
        public static final String CASH_DEPOSIT = "CASH_DEPOSIT";
        public static final String CASH_WITHDRAWAL = "CASH_WITHDRAWAL";
    }

    public static final int CREDIT = 1;
    public static final int DEBIT = 2;

    public static final String TRANSACTION_LIMIT_CROSS = "Transaction Amount is Above provided LIMIT";

    public static class TransactionStatus {
        public static final int INITIATED = 1 ;
        public static final int COMPLETED = 2 ;
        public static final int REJECTED = 3 ;
    }
    public static class  cashReserveStatus {
        public static final int ACTIVE = 1 ;
        public static final int EXPIRED = 2 ;
        public static final int BLOCKED = 3 ;
        public static final int CONFIRMED = 4 ;
    }

}