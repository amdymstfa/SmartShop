package com.microtech.smartshop.enums;

public enum OrderStatus {

    PENDING ,CONFIRMED ,CANCELED,REJECTED;

    public boolean isPending(){
        return this == PENDING;
    }

    public boolean isConfirmed(){
        return this == CONFIRMED ;
    }

    public boolean isCancelled(){
        return this == CANCELED ;
    }

    public boolean isRejected(){
        return this == REJECTED ;
    }
}