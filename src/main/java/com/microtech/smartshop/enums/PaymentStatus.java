package com.microtech.smartshop.enums;

public enum PaymentStatus {
    PENDING ,CASHED ,REJECTED, CLEARED;

    public boolean isPending(){
        return this == PENDING ;
    }

    public boolean isCashed(){
        return this == CASHED ;
    }

    public boolean isRejected(){
        return this == REJECTED ;
    }

    public boolean isCleared(){
        return this == CLEARED ;
    }
}