package com.microtech.smartshop.enums;

public enum PaymentType {
    CASH ,CHECK ,TRANSFER;

    public boolean isCash(){
        return this == CASH ;
    }

    public boolean isCheck(){
        return this == CHECK ;
    }

    public boolean isTransfer(){
        return this == TRANSFER ;
    }

}