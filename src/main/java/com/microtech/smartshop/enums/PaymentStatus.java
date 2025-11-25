package com.microtech.smartshop.enums;

public enum PaymentStatus {
    PENDING ,CASHED ,REJECTED;

    public boolean isPending(){
        return this == PENDING ;
    }

    public boolean isCashed(){
        return this == CASHED ;
    }

    public boolean isRejected(){
        return this == REJECTED ;
    }
}